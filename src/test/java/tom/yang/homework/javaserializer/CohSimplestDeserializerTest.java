package tom.yang.homework.javaserializer;

import java.nio.ByteBuffer;
import javax.xml.bind.DatatypeConverter;

import org.junit.Assert;
import org.junit.Test;



public class CohSimplestDeserializerTest {
    private static final byte[] testData;

    static {
        testData = DatatypeConverter.parseHexBinary(
                "aced0005737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f400000000000017708000000020000000174000a53657269616c697a6572740021636f6d2e74616e676f736f6c2e696f2e44656661756c7453657269616c697a6572");
    }

    @Test
    public void testDefault() {
        CohSimplestDeserializer d=new CohSimplestDeserializer();
        ByteBuffer buffer = ByteBuffer.wrap(testData);
        d.readObject(buffer);
        Assert.assertEquals(0, buffer.remaining());
    }

}
