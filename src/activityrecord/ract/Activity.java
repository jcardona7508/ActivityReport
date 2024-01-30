package activityrecord.ract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.StringTokenizer;

public class Activity implements Cloneable, Serializable {
  protected String acur = null;
  
  protected String nume = null;
  
  protected String fech = null;
  
  protected String acua = null;
  
  protected Float cana = null;
  
  protected String acuo = null;
  
  protected Float cano = null;
  
  protected String come = null;
  
  protected String stam = null;
  
  protected String acu4 = null;
  
  protected String detail = null;
  
  protected Boolean billable = Boolean.valueOf(false);
  
  protected BitSet customFields = new BitSet(5);
  
  protected ArrayList<String> customFieldValues = new ArrayList<>(5);
  
  public static final int MAX_CUSTOMFIELDS = 5;
  
  protected String actividad;
  
  protected String objeto;
  
  protected boolean fixed;
  
  protected String type;
  
  protected int registros;
  
  protected String acur_acur;
  
  protected String acur_usua;
  
  public Activity() {
    for (int i = 0; i < 5; i++)
      this.customFieldValues.add(""); 
    this.type = "D";
  }
  
  public String getAcu4() {
    return this.acu4;
  }
  
  public void setAcu4(String acu4) {
    this.acu4 = acu4;
  }
  
  public String getDetail() {
    return this.detail;
  }
  
  public void setDetail(String detail) {
    this.detail = detail;
  }
  
  public void setAcur(String acur) {
    this.acur = acur;
  }
  
  public String getAcur() {
    return this.acur;
  }
  
  public void setNume(String nume) {
    this.nume = nume;
  }
  
  public String getNume() {
    return this.nume;
  }
  
  public void setFech(String fech) {
    this.fech = fech;
  }
  
  public String getFech() {
    return this.fech;
  }
  
  public void setAcua(String acua) {
    this.acua = acua;
  }
  
  public String getAcua() {
    return this.acua;
  }
  
  public void setCana(String cana) {
    setCana(hourToDecimal(cana));
  }
  
  public void setCana(Float cana) {
    this.cana = cana;
  }
  
  public Float getCana() {
    return this.cana;
  }
  
  public void setAcuo(String acuo) {
    this.acuo = acuo;
  }
  
  public String getAcuo() {
    return this.acuo;
  }
  
  public void setCano(String cano) {
    setCano(hourToDecimal(cano));
  }
  
  public void setCano(Float cano) {
    if (cano != null) {
      this.cano = cano;
    } else {
      this.cano = this.cana;
    } 
  }
  
  public Float getCano() {
    return this.cano;
  }
  
  public void setCome(String come) {
    this.come = come;
  }
  
  public String getCome() {
    return this.come;
  }
  
  public void setStam(String stam) {
    this.stam = stam;
  }
  
  public String getStam() {
    return this.stam;
  }
  
  public void generateNumber(int i) {
    int resultado = 0;
    if (this.fech != null) {
      StringTokenizer fechacompleta = new StringTokenizer(this.fech, "/");
      int ano = (new Integer(fechacompleta.nextToken())).intValue();
      int mes = (new Integer(fechacompleta.nextToken())).intValue();
      int dia = (new Integer(fechacompleta.nextToken())).intValue();
      resultado = ((ano - 1900) * 10000 + mes * 100 + dia) * 1000 + i;
      setNume((new Integer(resultado)).toString());
    } 
  }
  
  private Float hourToDecimal(String canHoras) {
    String horas;
    float resultado = 0.0F;
    Float result = new Float(0.0F);
    if (canHoras == null)
      return result; 
    if (canHoras.startsWith(".")) {
      horas = "0" + canHoras;
    } else {
      horas = canHoras;
    } 
    if (horas.indexOf(":") >= 0) {
      String[] resp = horas.split(":");
      if (resp.length == 2) {
        float val1 = (new Float(resp[0])).floatValue();
        String dh = resp[1];
        float val2 = (new Float(dh)).floatValue();
        float val3 = val2 / 60.0F;
        resultado = val1 + val3;
        result = new Float(resultado);
      } 
    } else {
      result = new Float(horas);
    } 
    return result;
  }
  
  public String getActividad() {
    return this.actividad;
  }
  
  public boolean isFixed() {
    return this.fixed;
  }
  
  public String getObjeto() {
    return this.objeto;
  }
  
  public String getType() {
    return this.type;
  }
  
  public void setActividad(String string) {
    this.actividad = string;
  }
  
  public void setObjeto(String string) {
    this.objeto = string;
  }
  
  public int getRegistros() {
    return this.registros;
  }
  
  public void setFixed(boolean b) {
    this.fixed = b;
  }
  
  public void setRegistros(int integer) {
    this.registros = integer;
  }
  
  public void setType(String string) {
    this.type = string;
  }
  
  public Object clone() throws CloneNotSupportedException {
    activityrecord.ract.Activity copy = (activityrecord.ract.Activity)super.clone();
    copy.cana = new Float(this.cana.floatValue());
    copy.cano = new Float(this.cano.floatValue());
    return copy;
  }
  
  public String toString() {
    return "\n[(" + getAcua() + ":" + getCana() + ")(" + getAcuo() + ":" + getCano() + ")]";
  }
  
  public String getAcur_acur() {
    return this.acur_acur;
  }
  
  public void setAcur_acur(String acur_acur) {
    this.acur_acur = acur_acur;
  }
  
  public String getAcur_usua() {
    return this.acur_usua;
  }
  
  public void setAcur_usua(String acur_usua) {
    this.acur_usua = acur_usua;
  }
  
  public Boolean hasCustomField(int customFieldNumber) {
    return Boolean.valueOf(this.customFields.get(customFieldNumber));
  }
  
  public void enableCustomField(int customFieldNumber) {
    this.customFields.set(customFieldNumber);
  }
  
  public void setCustomFieldValue(int customFieldNumber, String value) {
    this.customFieldValues.set(customFieldNumber, value);
  }
  
  public String getCustomFieldValue(int customFieldNumber) {
    return this.customFieldValues.get(customFieldNumber);
  }
  
  public Boolean isBillable() {
    return this.billable;
  }
  
  public void setBillable(Boolean billable) {
    this.billable = billable;
  }
  
  public ArrayList<String> getCustomFieldValues() {
    return this.customFieldValues;
  }
}
