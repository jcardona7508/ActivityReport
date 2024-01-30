package activityrecord.html;

import common.util.ResourceString;
import siscorp.framework.application.Session;
import siscorp.webmanagement.HTMLSelectSC;

public class HTMLActivityColumns extends HTMLSelectSC {
  public HTMLActivityColumns(Session session) {
    String[] localValues = new String[0];
    localValues = new String[5];
    localValues[0] = "ACTIVITY.ACUM,ACUM.NOMBRE,ACUM.TIPO,ACUM.UNIDAD";
    localValues[1] = "ACTIVITY.ACUM";
    localValues[2] = "ACUM.NOMBRE";
    localValues[3] = "ACUM.TIPO";
    localValues[4] = "ACUM.UNIDAD";
    setValues(localValues);
    ResourceString i18nTexts = (ResourceString)session.getAttribute("RESOURCESTRING");
    String[] localOptions = new String[5];
    localOptions[0] = i18nTexts.getString("htmlColumns.All");
    localOptions[1] = i18nTexts.getString("htmlColumns.Code");
    localOptions[2] = i18nTexts.getString("htmlColumns.Name");
    localOptions[3] = i18nTexts.getString("htmlColumns.Type");
    localOptions[4] = i18nTexts.getString("htmlColumns.Unit");
    setOptions(localOptions);
    setName("columns");
    setClassName("opciones");
  }
}
