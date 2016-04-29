package tom.yang.homework.javaserializer;

/**
 * The type Coherence field description.
 *
 * @version 1.0
 * @since v3.4
 */
public interface PrintableField {

    /**
     * Gets field name.
     *
     * @return the field name
     */
    String getFieldName();

    /**
     * Gets field value.
     *
     * @return the field value
     */
    String getFieldValue();

    /**
     * Gets field depth.
     *
     * @return the field depth
     */
    int getFieldDepth();
}
