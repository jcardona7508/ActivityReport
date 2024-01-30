package activityrecord.html;

import activityrecord.ract.ActivityChoiceTO;
import common.util.ResourceString;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import siscorp.webmanagement.HTMLBuffer;
import siscorp.webmanagement.HTMLContentSC;
import siscorp.webmanagement.HTMLPrintWriterBuffer;
import siscorp.webmanagement.HTMLStringBuffer;

public class HTMLResourceChoices implements HTMLContentSC, Serializable {
  List choices;
  
  ResourceString resourceString;
  
  public HTMLResourceChoices(List activities, ResourceString resourceString) {
    this.choices = activities;
    this.resourceString = resourceString;
  }
  
  public String getHTMLContent() throws Exception {
    StringBuffer buffer = new StringBuffer(1000);
    getHTMLContent((HTMLBuffer)new HTMLStringBuffer(buffer));
    return buffer.toString();
  }
  
  public void getHTMLContent(PrintWriter out) throws Exception {
    getHTMLContent((HTMLBuffer)new HTMLPrintWriterBuffer(out));
  }
  
  public void getHTMLContent(HTMLBuffer buffer) {
    buffer.appendLine("<tr>");
    buffer.appendLine("<td width=\"28\" class=\"cht\">&nbsp;</td> ");
    buffer.appendLine("<td width=\"12\" class=\"cht\">&nbsp;</td>");
    buffer.appendLine("<td width=\"99\" class=\"cht\">" + 
        this.resourceString.getString("htmlChoices.Code") + "</td>");
    buffer.appendLine("<td width=\"313\" class=\"cht\">" + 
        this.resourceString.getString("htmlChoices.Name") + "</td>");
    buffer.appendLine("<td width=\"99\" class=\"cht\">" + 
        this.resourceString.getString("htmlChoices.Class") + "</td>");
    buffer.appendLine("<td width=\"103\" class=\"cht\">" + 
        this.resourceString.getString("htmlChoices.Unit") + "</td>");
    buffer.appendLine("</tr>");
    for (int i = 0; i < this.choices.size(); i++) {
      buffer.appendLine("  <tr>");
      ActivityChoiceTO choice = (ActivityChoiceTO) this.choices.get(i);
      buffer.append("\t<td width=\"28\" class=\"chtv\">");
      buffer.append(Integer.toString(i + 1));
      buffer.appendLine("\t</td>");
      buffer.appendLine("\t<td width=\"12\" class=\"chi\">");
      buffer.append("\t\t<img src=\"/activityreportdoc/images/button.png\" width=\"8\" height=\"8\" class=\"choice\" id=\"");
      buffer.append(choice.codigo.trim());
      buffer.appendLine("\">");
      buffer.append("\t\t<input type=\"hidden\" name=\"sel\" id=\"i_");
      buffer.append(choice.codigo.trim());
      buffer.appendLine("\" value=\"\" >");
      buffer.appendLine("\t</td>");
      buffer.append("\t<td width=\"99\" class=\"ch\">");
      buffer.append(choice.codigo.trim());
      buffer.appendLine("\t</td>");
      buffer.append("\t<td width=\"313\" class=\"ch\">");
      buffer.append(choice.nombre.trim());
      buffer.appendLine("\t</td>");
      buffer.append("\t<td width=\"100\" class=\"ch\">");
      buffer.append(choice.tipo.trim());
      buffer.appendLine("\t</td>");
      buffer.append("\t<td width=\"100\" class=\"ch\">");
      if (choice.unidad == null) {
        buffer.append("HRS");
      } else {
        buffer.append(choice.unidad.trim());
      } 
      buffer.appendLine("\t</td>");
      buffer.appendLine("</tr>");
    } 
  }
}
