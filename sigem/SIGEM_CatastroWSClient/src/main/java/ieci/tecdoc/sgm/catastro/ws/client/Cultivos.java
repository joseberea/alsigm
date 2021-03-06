/**
 * Cultivos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package ieci.tecdoc.sgm.catastro.ws.client;

public class Cultivos  extends ieci.tecdoc.sgm.catastro.ws.client.RetornoServicio  implements java.io.Serializable {
    private ieci.tecdoc.sgm.catastro.ws.client.Cultivo[] cultivos;

    public Cultivos() {
    }

    public Cultivos(
           java.lang.String errorCode,
           java.lang.String returnCode,
           ieci.tecdoc.sgm.catastro.ws.client.Cultivo[] cultivos) {
        super(
            errorCode,
            returnCode);
        this.cultivos = cultivos;
    }


    /**
     * Gets the cultivos value for this Cultivos.
     * 
     * @return cultivos
     */
    public ieci.tecdoc.sgm.catastro.ws.client.Cultivo[] getCultivos() {
        return cultivos;
    }


    /**
     * Sets the cultivos value for this Cultivos.
     * 
     * @param cultivos
     */
    public void setCultivos(ieci.tecdoc.sgm.catastro.ws.client.Cultivo[] cultivos) {
        this.cultivos = cultivos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Cultivos)) return false;
        Cultivos other = (Cultivos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.cultivos==null && other.getCultivos()==null) || 
             (this.cultivos!=null &&
              java.util.Arrays.equals(this.cultivos, other.getCultivos())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getCultivos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCultivos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCultivos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Cultivos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.ws.catastro.sgm.tecdoc.ieci", "Cultivos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cultivos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://server.ws.catastro.sgm.tecdoc.ieci", "cultivos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://server.ws.catastro.sgm.tecdoc.ieci", "Cultivo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://server.ws.catastro.sgm.tecdoc.ieci", "item"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
