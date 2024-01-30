package activityrecord.data;

public class Condition {
  public String createCondition(String[] columns, int option, String filter) {
    String condicion = "";
    if (columns.length == 0 || filter.equals(""))
      return condicion; 
    if (option == 0)
      filter = " LIKE '" + filter.toUpperCase() + "%'"; 
    if (option == 1)
      filter = "='" + filter.toUpperCase() + "'"; 
    if (option == 2)
      filter = " LIKE '%" + filter.toUpperCase() + "%'"; 
    condicion = "AND ( ";
    int i = 0;
    while (i < columns.length) {
      condicion = String.valueOf(condicion) + "upper(" + columns[i] + ")" + filter;
      if (columns.length > 1 && i < columns.length - 1)
        condicion = String.valueOf(condicion) + " OR "; 
      i++;
    } 
    condicion = String.valueOf(condicion) + " )";
    return condicion;
  }
}
