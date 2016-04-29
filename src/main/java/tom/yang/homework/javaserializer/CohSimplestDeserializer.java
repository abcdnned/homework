package tom.yang.homework.javaserializer;

import java.io.IOException;
import static java.io.ObjectStreamConstants.*;
import java.io.StreamCorruptedException;
import java.io.WriteAbortedException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.netis.dcd.parser.util.buffer.AsciiBuffer;

public class CohSimplestDeserializer implements CohDeserializer {

    private int depth = 0;

    private int pass;

    /** The constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(CohSimplestDeserializer.class);

    @Override
    public List<PrintableField> readObject(ByteBuffer buffer, boolean printClazz,
            boolean printValue, int limit) throws IllegalArgumentException {
        pass = buffer.position();

        short magic = buffer.getShort();
        short version = buffer.getShort();

        bufferlog(buffer);

        LOG.info("magic num " + Integer.toHexString(magic));
        LOG.info("version " + Integer.toHexString(version));

        try {
            return (List<PrintableField>) read0(buffer, printClazz, printValue, limit);
        } catch (IOException e) {
            LOG.warn("readObject failed.");
            return null;
        }
    }

    private void bufferlog(ByteBuffer buffer){
        int len = buffer.position() - pass;
        byte[] bytes = new byte[len];
        System.arraycopy(buffer.array(), pass, bytes, 0, len);
        LOG.info("buffer consume : " + DatatypeConverter.printHexBinary(bytes));
        pass = buffer.position();
    }

    private Object read0(ByteBuffer buffer, boolean printClazz,
            boolean printValue, int limit) throws IOException {
        try {
            byte tc;
            while ((tc = peek(buffer)) == TC_RESET) {
                buffer.get();
                // handleReset();
            }

            depth++;

            switch (tc) {
            case TC_NULL:
                return readNull(buffer);

            case TC_REFERENCE:
                return readHandle(buffer);

            case TC_CLASS:
                return readClass(buffer);

            case TC_CLASSDESC:
            case TC_PROXYCLASSDESC:
                return readClassDesc(buffer);

            case TC_STRING:
            case TC_LONGSTRING:
                return readString();

            case TC_OBJECT:
                return readOrdinaryObject(buffer);

            case TC_EXCEPTION:
                IOException ex = readFatalException();
                throw new WriteAbortedException("writing aborted", ex);

            case TC_BLOCKDATA:
            case TC_BLOCKDATALONG:
                throw new StreamCorruptedException(
                        "unexpected block data");
            case TC_ENDBLOCKDATA:
                throw new StreamCorruptedException(
                        "unexpected end of block data");
            default:
                throw new StreamCorruptedException(
                        String.format("invalid type code: %02X", tc));
            }
        } finally {
            depth--;
        }
    }

    private IOException readFatalException() {
        return null;
    }

    private Object readOrdinaryObject(ByteBuffer buffer) throws IOException {
        if (buffer.get() != TC_OBJECT) {
            throw new InternalError();
        }

        bufferlog(buffer);

        readClassDesc(buffer);
        readSerialData(buffer);
        bufferlog(buffer);
        return null;
    }


    private void readSerialData(ByteBuffer buffer) {
        buffer.getLong();
    }

    private Object readString() {
        // TODO Auto-generated method stub
        return null;
    }

    private void skipCustomData(ByteBuffer buffer) throws IOException {
        for (;;) {
            switch (peek(buffer)) {
            case TC_BLOCKDATA:
            case TC_BLOCKDATALONG:
                break;

            case TC_ENDBLOCKDATA:
                buffer.get();
                return;

            default:
                read0(buffer, true, true, -1);
                break;
            }
        }
    }


    private Object readClassDesc(ByteBuffer buffer) throws IOException {
        byte tc = peek(buffer);
        switch (tc) {
        case TC_NULL:
            return readNull(buffer);

        case TC_REFERENCE:
            return readHandle(buffer);

        case TC_PROXYCLASSDESC:
            return readProxyDesc();

        case TC_CLASSDESC:
            return readNonProxyDesc(buffer);

        default:
            throw new StreamCorruptedException(
                    String.format("invalid type code: %02X", tc));
        }
    }

    private Object readNonProxyDesc(ByteBuffer buffer) throws IOException {
        if (buffer.get() != TC_CLASSDESC) {
            throw new InternalError();
        }
        bufferlog(buffer);

        String name = readUTF(buffer);
        bufferlog(buffer);
        long suid = buffer.getLong();
        bufferlog(buffer);
        byte flags = buffer.get();
        bufferlog(buffer);
        int numFields = buffer.getShort();
        bufferlog(buffer);

        for (int i = 0; i < numFields; i++) {
            char tcode = (char) buffer.get();
            String fname = readUTF(buffer);
            bufferlog(buffer);
        }

        skipCustomData(buffer);
        bufferlog(buffer);

        readClassDesc(buffer);
        bufferlog(buffer);

        return null;
    }

    private String readUTF(ByteBuffer buffer) {
        short s = buffer.getShort();
        String result = AsciiBuffer.getString(buffer, s);
        LOG.info(result);
        return result;
    }

    private Object readProxyDesc() {
        // TODO Auto-generated method stub
        return null;
    }

    private Object readClass(ByteBuffer buffer) throws IOException {
        if (buffer.get() != TC_CLASS) {
            throw new InternalError();
        }
        String desc = (String) readClassDesc(buffer);
        return desc;
    }

    private Object readHandle(ByteBuffer buffer) {
        if (buffer.get() != TC_REFERENCE) {
            throw new InternalError();
        }
        buffer.getInt();

        return null;
    }

    private Object readNull(ByteBuffer buffer) {
        if (buffer.get() != TC_NULL) {
            throw new InternalError();
        }
        return null;
    }

    private byte peek(ByteBuffer buffer){
        if(buffer.remaining()>0){
            int p=buffer.position();
            byte result=buffer.get();
            buffer.position(p);
            return result;
        }else{
            throw new IllegalArgumentException("can't peek one byte,ramaining bytes less than 1.");
        }
    }


    @Override
    public void skipObject(ByteBuffer buffer) throws IllegalArgumentException {
    }
}
