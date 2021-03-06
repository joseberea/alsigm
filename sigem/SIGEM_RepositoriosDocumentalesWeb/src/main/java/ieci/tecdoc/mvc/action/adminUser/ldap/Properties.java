/*
 * Created on 04-abr-2005
 *
 */
package ieci.tecdoc.mvc.action.adminUser.ldap;

import ieci.tecdoc.core.db.DBSessionManager;
import ieci.tecdoc.core.db.DbConnection;
import ieci.tecdoc.core.db.DbConnectionConfig;
import ieci.tecdoc.core.ldap.LdapAttribute;
import ieci.tecdoc.core.ldap.LdapConnCfg;
import ieci.tecdoc.core.ldap.LdapConnection;
import ieci.tecdoc.core.ldap.LdapFilter;
import ieci.tecdoc.idoc.admin.api.ObjFactory;
import ieci.tecdoc.idoc.admin.api.user.LdapGroup;
import ieci.tecdoc.idoc.admin.api.user.LdapUser;
import ieci.tecdoc.mvc.action.BaseAction;
import ieci.tecdoc.mvc.dto.adminUser.ldap.PropertiesDTO;
import ieci.tecdoc.mvc.form.adminUser.ldap.UserForm;
import ieci.tecdoc.mvc.service.ServiceTree;
import ieci.tecdoc.mvc.service.ServiceTreeLdap;
import ieci.tecdoc.mvc.service.adminUser.ldap.ServiceLdap;
import ieci.tecdoc.mvc.util.Constantes;
import ieci.tecdoc.mvc.util.NaryTree;
import ieci.tecdoc.mvc.util.Node;
import ieci.tecdoc.mvc.util.SessionHelper;
import ieci.tecdoc.sbo.config.CfgMdoConfig;
import ieci.tecdoc.sbo.uas.ldap.UasConfigUtil;

import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Antonio Mar�a
 *
 */
public class Properties extends BaseAction{

    private static Logger logger = Logger.getLogger(Properties.class);
    /* (non-Javadoc)
     * @see ieci.tecdoc.mvc.action.BaseAction#executeLogic(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    ServiceTree serviceTree;
    ServiceLdap serviceLdap;
    
    protected ActionForward executeLogic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	String entidad=SessionHelper.getEntidad(request);
    	
    	if (logger.isDebugEnabled())
            logger.debug("Properties Action");
        
        String id = request.getParameter("id");
        String Dn = request.getParameter("dn"); // es nulo, excepto si se obtiene de una busqueda por ldap
        
        //System.out.println ("dn: " + dn);
        PropertiesDTO pBean = null;
        HttpSession session = request.getSession();
        NaryTree tree = (NaryTree) session.getAttribute("tree");
        int maxChildrenLdap = ( (Integer)request.getSession().getServletContext().getAttribute(Constantes.MAX_CHILDREN_LDAP) ).intValue();
        serviceTree = new ServiceTreeLdap(tree, maxChildrenLdap, entidad);
        serviceLdap = new ServiceLdap (maxChildrenLdap, entidad);
        
        if (id != null)
        {
	        boolean enc = serviceTree.searchNode(tree, Integer.parseInt(id));
	        
	        if (enc){
	            
	         Node nodo = (Node) serviceTree.fin.getRoot();
	         //System.out.println (nodo.getValor());
	         Dn = nodo.getTitle(); // Sacar el dn a partir del id del �rbol
	         
	        }
        }
        
        if (logger.isDebugEnabled())
            logger.debug("Propiedades de: "+ Dn );
        
        UserForm userform= (UserForm) form;
        
        NamingEnumeration atts = serviceLdap.getObjectAttributes(Dn);
        DbConnectionConfig dbconf=null;
        LdapConnection ldapConn = new LdapConnection();
        LdapConnCfg   connCfg = null; 
        
        
           DbConnection.open(DBSessionManager.getSession(entidad));
           connCfg = UasConfigUtil.createLdapConnConfig(entidad);
    		ldapConn.open(connCfg);
        Map map = new TreeMap();
        

             while (atts.hasMore())
             {
                 
             	 Attribute o = (Attribute) atts.nextElement();
             	 String clave="";
             	 String valor="";
             	 clave=o.getID().toLowerCase();;
             	 if (LdapAttribute.getGuidAttributeName(ldapConn).toLowerCase().equals(clave)){
             	 	Enumeration values = o.getAll();
             	 	Object element=values.nextElement();
             	 	valor=ieci.tecdoc.core.ldap.LdapBasicFns.formatGuid(ldapConn,element);
             	 }
             	 else{
             	 
             	 //if ( o instanceof  byte[]){
             	 //	String otro=ieci.tecdoc.core.ldap.LdapBasicFns.formatGuid(ldapConn,o);
             	 //}
             	 String s = o.toString();
                 StringTokenizer st = new StringTokenizer(s, ":");
                 clave = st.nextToken().toLowerCase().trim();
                 valor = st.nextToken().trim();
             	 }
                 map.put(clave,valor);
             }
         
         pBean = new PropertiesDTO();
         pBean.setDn(Dn);
         pBean.setAttsMap(map);
         

        
        
        String Guid;
        String valorGuid="";
        
        String userFilter = LdapFilter.getUserFilter(ldapConn);
        int i = userFilter.indexOf("=");
        userFilter = userFilter.substring(i+1,userFilter.length()-1);
        
        String groupFilter = LdapFilter.getGroupFilter(ldapConn);
        i = groupFilter.indexOf("=");
        groupFilter = groupFilter.substring(i+1,groupFilter.length()-1);
        if (logger.isDebugEnabled()){
            logger.debug("objClass: " + map.get("objectclass").toString().toLowerCase());
        }
        
        if (map.get("objectclass").toString().toLowerCase().indexOf("person") != -1) // Si es persona tng q saber si esta dada de alta en invesdoc
        {
            Guid=LdapAttribute.getGuidAttributeName(ldapConn).toLowerCase(); //segun motor
            //String nombreAtributo="";
            //String valorAtributo;
            
            userform.setGuid(valorGuid);
            logger.debug("Guid:" + Guid );
            valorGuid = map.get(Guid).toString();
            //valorAtributo = map.get(nombreAtributo).toString();

            //valorGuid 
            //System.out.println ("Guid->" + Guid + ":" + valorGuid + " #" + valorGuid.length());
            LdapUser user =ObjFactory.createLdapUser(0);
            pBean.setEsUsuario(true);
            pBean.setValorGuid(valorGuid);


            // userAttName Obtener el userAttName (uid) para insertarlo en la tabla de certificados
            String userAttNameKey =  session.getServletContext().getAttribute("userAttNameKey").toString();
            String userAttNameValue = map.get(userAttNameKey).toString();
            session.setAttribute("userAttName", userAttNameValue);
            // fin userAttName
            
            try {
            	//String valorGuid_m= ieci.tecdoc.core.ldap.LdapBasicFns.formatGuid(ldapConn,valorGuid);
                user.loadFromGuid(valorGuid, entidad);
                pBean.setDadoDeAlta(true);
            
            } catch (Exception e2) { // eL usuario de Ldap, no esta dado de alta en invesDoc
                pBean.setNombreAtributo(Guid);
                pBean.setValorAtributo(valorGuid);
                
                request.setAttribute("propiedades", pBean);
                return (mapping.findForward("success"));
            }
            
        }
        else if (map.get("objectclass").toString().toLowerCase().indexOf(groupFilter) != -1)
        {
            System.out.println ("Propiedades Grupos");
            String rootuser=null;
            
            // Intentar ver si el grupo pertenece a invesDoc
            // rootuser=UasConfigUtil.createUasAuthConfig().getUserStart();
            
        	
            Guid = LdapAttribute.getGuidAttributeName(ldapConn).toLowerCase(); // recupera dl api el atributo con valor unico para la entrada en funcion del motor ldap
            // Guid = uidnumber para OpenLdap
            LdapGroup ldapGroup = ObjFactory.createLdapGroup(0);
            
            valorGuid = map.get(Guid).toString();
            userform.setGuid(valorGuid);
            
            pBean.setEsGrupo(true);
            pBean.setValorGuid(valorGuid);
            try {
                ldapGroup.loadFromGuid(valorGuid, entidad);
                pBean.setDadoDeAlta(true);
                logger.debug("Grupo dado de alta" );
            
            }catch (Exception e){
                logger.debug("Grupo no dado de alta");
                
                pBean.setNombreAtributo(Guid);
                pBean.setValorAtributo(valorGuid);
                request.setAttribute("propiedades", pBean);
                return (mapping.findForward("success"));
            }
            
            
        }
            
        request.setAttribute("propiedades", pBean);
        
        return mapping.findForward("success");
        
    }

}
