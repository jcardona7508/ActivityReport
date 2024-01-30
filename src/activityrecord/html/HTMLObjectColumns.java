package activityrecord.html;

import common.util.ResourceString;
import siscorp.framework.application.Session;
import siscorp.webmanagement.HTMLSelectSC;

public class HTMLObjectColumns extends HTMLSelectSC {
  public HTMLObjectColumns(Session session) {
    String[] localValues = new String[0];
    localValues = new String[3];
    localValues[0] = "OBJECT.ACUM,ACUM.NOMBRE";
    localValues[1] = "OBJECT.ACUM";
    localValues[2] = "ACUM.NOMBRE";
    setValues(localValues);
    ResourceString i18nTexts = (ResourceString)session.getAttribute("RESOURCESTRING");
    String[] localOptions = new String[3];
    localOptions[0] = i18nTexts.getString("htmlColumns.All");
    localOptions[1] = i18nTexts.getString("htmlColumns.Code");
    localOptions[2] = i18nTexts.getString("htmlColumns.Name");
    setOptions(localOptions);
    setName("columns");
    setClassName("opciones");
  }
}
