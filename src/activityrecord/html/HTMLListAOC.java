package activityrecord.html;

import java.io.PrintWriter;
import java.util.StringTokenizer;
import siscorp.dbmanagement.DatabaseSC;
import siscorp.dbmanagement.ResultSetSC;
import siscorp.webmanagement.HTMLElementSC;
import siscorp.webmanagement.HTMLQueryListBoxSC;

public class HTMLListAOC extends HTMLQueryListBoxSC {
  protected HTMLQueryListBoxSC listBox;
  
  public HTMLListAOC(DatabaseSC db, String usuario, String clase, String tipo, String nombre) throws Exception {
    setQuery((ResultSetSC)db.getNewQuery("SELECT ACUM.CODIGO,ACUM.NOMBRE,ACUM.TIPO FROM GASI ACTIV , GASI RECU,ACUM WHERE ACTIV.CLAS = '" + 
          
          clase + "' AND RECU.CLAS = 'R'" + 
          " AND ACTIV.GDEF = RECU.GDEF" + 
          " AND RECU.ACUM = '" + usuario + "'" + 
          " AND" + 
          " ACTIV.ACUM = ACUM.CODIGO AND" + 
          " ACUM.TIPO IN (" + ParametrosObjeto(tipo) + ")"));
    setProperty("name", nombre);
    setProperty("width", "14%");
    setValueColumn("codigo");
    setContentColumn("nombre");
    setProperty("class", "field");
  }
  
  public HTMLListAOC(DatabaseSC db, String idSelected, String usuario, String clase, String tipo, String nombre) throws Exception {
    this(db, usuario, clase, tipo, nombre);
    setSelectedValue("codigo", idSelected);
  }
  
  public String getHTMLContent() throws Exception {
    return super.getHTMLContent();
  }
  
  public void getQueryContent(PrintWriter out) throws Exception {
    HTMLElementSC opt = new HTMLElementSC("Option", "none");
    opt.setProperty("value", "-1");
    opt.getHTMLContent(out);
    getHTMLContent(out);
  }
  
  public String ParametrosObjeto(String tipo) {
    String tipos = "";
    StringTokenizer st = new StringTokenizer(tipo, ",");
    while (st.hasMoreTokens())
      tipos = String.valueOf(tipos) + "'" + st.nextToken() + "'" + (st.hasMoreTokens() ? "," : ""); 
    return tipos;
  }
}
