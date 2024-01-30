package activityrecord.html;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Generador {
  static ArrayList<String> lines;
  
  static String[] borderType = new String[] { "2px solid BLACK;", 
      "1px solid RGB(192,192,192);", 
      "1px solid RGB(128,128,128);", 
      "2px solid RGB(192,164,164);", 
      "1px solid RGB(160,170,15);", 
      "1px solid RGB(208,255,16);", 
      "1px solid RGB(192,192,192);", 
      "1px solid WHITE;" };
  
  static final int BORDE_GRUESO = 0;
  
  static final int BORDE_DATOS = 1;
  
  static final int BORDE_ACTIV = 2;
  
  static final int BORDE_SEPARA = 3;
  
  static final int BORDE_SELECT = 4;
  
  static final int BORDE_LABEL_SELECT = 5;
  
  static final int BORDE_ACTIVEDIT = 6;
  
  static final int BORDE_CLEAR = 7;
  
  static String[] colorValue = new String[] { 
      "WHITE", 
      "RGB(224,232,240)", 
      "RGB(228,244,250)", 
      "RGB(255,255,204)", 
      "RGB(96,128,192)", 
      "RGB(0,0,128)", 
      "RGB(128,0,0)", 
      "RGB(32,32,128)", 
      "RGB(235,235,240)", 
      "RGB(32,32,128)", 
      "RGB(128,128,192)", 
      "RGB(216,216,216)", 
      "RGB(216,216,164)", 
      "RGB(248,248,32)", 
      "WHITE", 
      "RGB(0,0,128)", 
      "WHITE", 
      "BLACK", 
      "WHITE", 
      "RGB(136,136,72)", 
      "BLACK", 
      "WHITE", 
      "RGB(192,216,226)", 
      "RGB(208,228,232)", 
      "RGB(248,255,248)" };
  
  static final int FONDO_CLEAR = 0;
  
  static final int FONDO_PADRE1 = 1;
  
  static final int FONDO_PADRE2 = 2;
  
  static final int FONDO_SELECT = 3;
  
  static final int FONDO_WORKDAY = 4;
  
  static final int FONDO_SATURDAY = 5;
  
  static final int FONDO_SUNDAY = 6;
  
  static final int FONDO_COMMENTS = 7;
  
  static final int FONDO_READONLY = 8;
  
  static final int FONDO_TOTPADRE1 = 9;
  
  static final int FONDO_TOTPADRE2 = 10;
  
  static final int FONDO_TOTDETALLE = 11;
  
  static final int FONDO_NUMEROS = 12;
  
  static final int TEXTO_TOTAL_HORAS = 13;
  
  static final int FONDO_TITULOTOTAL = 14;
  
  static final int FONDO_TITULOCOMMENTS = 15;
  
  static final int TEXTO_TITULO_LINK = 16;
  
  static final int TEXTO_NORMAL = 17;
  
  static final int TEXTO_WHITE = 18;
  
  static final int TEXTO_NUMEROS = 19;
  
  static final int TEXTO_NUMEROSSEL = 20;
  
  static final int FONDO_INPUT_NORMAL = 21;
  
  static final int FONDO_PADRE1SEL = 22;
  
  static final int FONDO_PADRE2SEL = 23;
  
  static final int FONDO_LEVEL3 = 24;
  
  static final String NO_MARGIN = " margin: 0px 0px 0px 0px;";
  
  static final String INPUT_NO_BORDERS = " border-style: outset; border-width: 0px 0px 0px 0px;";
  
  static final String NORMAL_PADDING = " padding: 0px 0px 2px 0px;";
  
  static final String NO_PADDING = " padding: 0px 0px 0px  0px;";
  
  static final String bigFontSize = "12px";
  
  static final String mediumFontSize = "11px";
  
  static final String smallFontSize = "10px";
  
  static final String TITLE1_FONT = " font-size:12px;";
  
  static final String TITLE2_FONT = " font-size:12px;";
  
  static final String TITLE3_FONT = " font-size:12px;";
  
  static final String COMMENTS_TITLE_FONT = " font-size:12px;";
  
  static final String INPUT_FONT = " font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal;";
  
  static final String SELECT_FONT = " font-family: Arial, Helvetica, sans-serif; font-size: 10px; font-weight: normal;";
  
  static final String WIDTH_99PERC = " width:99%;";
  
  static final String FONT_BOLD = " font-weight:bold;";
  
  static final String ALIGN_LEFT = " text-align:left;";
  
  static final String ALIGN_CENTER = " text-align:center;";
  
  static final String ALIGN_RIGHT = " text-align:right;";
  
  static final String NO_SELECT_BORDER = " outline:none;";
  
  private static String agregarBorde(int bordes, int tipo) {
    String result = "";
    if ((bordes & 0x1) > 0)
      result = String.valueOf(result) + " border-top: " + borderType[tipo]; 
    if ((bordes & 0x2) > 0)
      result = String.valueOf(result) + " border-right: " + borderType[tipo]; 
    if ((bordes & 0x4) > 0)
      result = String.valueOf(result) + " border-bottom: " + borderType[tipo]; 
    if ((bordes & 0x8) > 0)
      result = String.valueOf(result) + " border-left: " + borderType[tipo]; 
    return result;
  }
  
  private static String agregarFondo(int tipoFondo) {
    return " background-color:" + colorValue[tipoFondo] + ";";
  }
  
  private static String agregarColor(int tipoColor) {
    return " color:" + colorValue[tipoColor] + ";";
  }
  
  private static void crearEstiloConSelIgual(String nombre, String contenido) {
    crearEstilo(nombre, contenido);
    crearEstilo(String.valueOf(nombre) + "SEL", contenido);
  }
  
  private static void crearEstilo(String nombre, String contenido) {
    lines.add(String.valueOf(nombre) + "{ ");
    lines.add("  " + contenido);
    lines.add("}");
    lines.add("");
  }
  
  public static void main(String[] args) {
    lines = new ArrayList<>();
    try {
      FileWriter outFile = new FileWriter("C:\\estilos.txt");
      PrintWriter out = new PrintWriter(outFile);
      String contenido = String.valueOf(agregarFondo(0)) + " padding: 0px 0px 2px 0px;";
      crearEstiloConSelIgual("td.tiNB", contenido);
      contenido = String.valueOf(agregarColor(17)) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;" + " font-size:12px;";
      crearEstilo("td.tiLVL1", contenido);
      contenido = String.valueOf(agregarColor(17)) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;" + " font-size:12px;";
      crearEstilo("td.tiLVL2", contenido);
      contenido = String.valueOf(agregarColor(17)) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;" + " font-size:12px;";
      crearEstilo("td.tiLVL3", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarColor(17) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstiloConSelIgual("td.tiTG", contenido);
      contenido = String.valueOf(agregarBorde(9, 0)) + agregarColor(17) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;" + " text-align:center;";
      crearEstiloConSelIgual("td.tiEXTRA", contenido);
      contenido = String.valueOf(agregarFondo(4)) + 
        " padding: 0px 0px 2px 0px;" + " text-align:center;";
      crearEstiloConSelIgual("td.tiDT", contenido);
      contenido = String.valueOf(agregarFondo(5)) + 
        " padding: 0px 0px 2px 0px;" + " text-align:center;";
      crearEstiloConSelIgual("td.tiSA", contenido);
      contenido = String.valueOf(agregarFondo(6)) + 
        " padding: 0px 0px 2px 0px;" + " text-align:center;";
      crearEstiloConSelIgual("td.tiSU", contenido);
      contenido = String.valueOf(agregarFondo(7)) + agregarColor(16) + 
        " padding: 0px 0px 2px 0px;" + " font-size:12px;";
      crearEstiloConSelIgual("td.tiCO", contenido);
      contenido = String.valueOf(agregarFondo(14)) + agregarColor(17) + 
        " padding: 0px 0px 2px 0px;" + " text-align:center;";
      crearEstiloConSelIgual("td.tiTO", contenido);
      contenido = String.valueOf(agregarFondo(0)) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + agregarColor(19);
      crearEstilo("td.laNUM", contenido);
      contenido = String.valueOf(agregarFondo(12)) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + agregarColor(20);
      crearEstilo("td.laNUMSEL", contenido);
      contenido = String.valueOf(agregarBorde(2, 0)) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstiloConSelIgual("td.tiRG", contenido);
      contenido = String.valueOf(agregarBorde(3, 0)) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstiloConSelIgual("td.tiTRG", contenido);
      contenido = String.valueOf(agregarBorde(8, 0)) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstiloConSelIgual("td.tiLG", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + 
        agregarFondo(1) + " padding: 0px 0px 2px 0px;" + " font-weight:bold;";
      crearEstilo("td.laP1NO", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(2, 3) + 
        agregarFondo(1) + " padding: 0px 0px 2px 0px;" + " font-weight:bold;";
      crearEstilo("td.laP1SE", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(2, 1) + 
        agregarFondo(1) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daP1ED", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(2, 1) + 
        agregarFondo(22) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daP1EDSEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarColor(18) + 
        agregarFondo(9) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstiloConSelIgual("td.daP1TO", contenido);
      contenido = String.valueOf(agregarBorde(1, 2)) + 
        agregarFondo(2) + " padding: 0px 0px 2px 0px;" + " font-weight:bold;";
      crearEstilo("td.laP2NO", contenido);
      contenido = String.valueOf(agregarBorde(1, 2)) + agregarBorde(2, 3) + 
        agregarFondo(2) + " padding: 0px 0px 2px 0px;" + " font-weight:bold;";
      crearEstilo("td.laP2SE", contenido);
      contenido = String.valueOf(agregarBorde(1, 2)) + agregarBorde(2, 1) + 
        agregarFondo(2) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daP2ED", contenido);
      contenido = String.valueOf(agregarBorde(1, 2)) + agregarBorde(2, 1) + 
        agregarFondo(23) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daP2EDSEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 2)) + agregarColor(18) + 
        agregarFondo(10) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstiloConSelIgual("td.daP2TO", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(4, 7) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL1NO", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(4, 7) + 
        agregarBorde(2, 3) + agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL1SE", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(4, 7) + 
        agregarBorde(2, 1) + agregarFondo(0) + " padding: 0px 0px 2px 0px;" + " text-align:right;";
      crearEstilo("td.daL1ED", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(4, 7) + 
        agregarBorde(2, 1) + agregarFondo(8) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.daL1RO", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + 
        agregarFondo(11) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daL1TO", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(4, 5) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL1NOSEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(4, 5) + 
        agregarBorde(2, 3) + agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL1SESEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(4, 7) + 
        agregarBorde(2, 1) + agregarFondo(3) + " padding: 0px 0px 2px 0px;" + " text-align:right;";
      crearEstilo("td.daL1EDSEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + agregarBorde(4, 7) + 
        agregarBorde(2, 1) + agregarFondo(8) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.daL1ROSEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 0)) + 
        agregarFondo(11) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daL1TOSEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + agregarBorde(4, 7) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL2NO", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + agregarBorde(4, 7) + 
        agregarBorde(2, 3) + agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL2SE", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + agregarBorde(4, 7) + 
        agregarBorde(2, 1) + agregarFondo(0) + " padding: 0px 0px 2px 0px;" + " text-align:right;";
      crearEstilo("td.daL2ED", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + agregarBorde(4, 7) + 
        agregarBorde(2, 1) + agregarFondo(8) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.daL2RO", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + 
        agregarFondo(11) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daL2TO", contenido);
      contenido = String.valueOf(agregarBorde(4, 5)) + agregarBorde(1, 6) + 
        agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL2NOSEL", contenido);
      contenido = String.valueOf(agregarBorde(4, 5)) + agregarBorde(2, 3) + 
        agregarBorde(1, 6) + agregarFondo(0) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL2SESEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + agregarBorde(4, 7) + 
        agregarBorde(2, 1) + agregarFondo(3) + " padding: 0px 0px 2px 0px;" + " text-align:right;";
      crearEstilo("td.daL2EDSEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + agregarBorde(4, 7) + 
        agregarBorde(2, 1) + agregarFondo(8) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.daL2ROSEL", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + 
        agregarFondo(11) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daL2TOSEL", contenido);
      contenido = String.valueOf(agregarBorde(4, 7)) + agregarBorde(1, 6) + 
        agregarFondo(24) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL3NO", contenido);
      contenido = String.valueOf(agregarBorde(4, 7)) + agregarBorde(2, 3) + 
        agregarBorde(1, 6) + agregarFondo(24) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL3SE", contenido);
      contenido = String.valueOf(agregarBorde(4, 7)) + agregarBorde(3, 1) + 
        agregarFondo(24) + " padding: 0px 0px 2px 0px;" + " text-align:right;";
      crearEstilo("td.daL3ED", contenido);
      contenido = String.valueOf(agregarBorde(4, 7)) + agregarBorde(3, 1) + 
        agregarFondo(8) + " padding: 0px 0px 2px 0px;" + " text-align:right;";
      crearEstilo("td.daL3RO", contenido);
      contenido = String.valueOf(agregarFondo(11)) + " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daL3TO", contenido);
      contenido = String.valueOf(agregarBorde(1, 6)) + agregarBorde(4, 5) + 
        agregarFondo(24) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL3NOSEL", contenido);
      contenido = String.valueOf(agregarBorde(4, 5)) + agregarBorde(2, 3) + 
        agregarBorde(1, 6) + agregarFondo(24) + " padding: 0px 0px 2px 0px;";
      crearEstilo("td.laL3SESEL", contenido);
      contenido = String.valueOf(agregarBorde(3, 1)) + agregarBorde(4, 7) + 
        agregarFondo(3) + " padding: 0px 0px 2px 0px;" + " text-align:right;";
      crearEstilo("td.daL3EDSEL", contenido);
      contenido = String.valueOf(agregarBorde(3, 1)) + agregarBorde(4, 7) + 
        agregarFondo(8) + " padding: 0px 0px 2px 0px;" + " text-align:right;";
      crearEstilo("td.daL3ROSEL", contenido);
      contenido = String.valueOf(agregarFondo(11)) + 
        " padding: 0px 0px 2px 0px;" + " text-align:right;" + " font-weight:bold;";
      crearEstilo("td.daL3TOSEL", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(21) + " text-align:right;" + " font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal;" + " width:99%;";
      crearEstilo("input.qty", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(3) + " text-align:right;" + " font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal;" + " width:99%;";
      crearEstilo("input.qtySEL", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(8) + " text-align:right;" + " font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal;" + " width:99%;";
      crearEstiloConSelIgual("input.qtyRO", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(21) + " text-align:left;" + " font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal;" + " width:99%;";
      crearEstilo("input.cus", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(3) + " text-align:left;" + " font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal;" + " width:99%;";
      crearEstilo("input.cusSEL", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(8) + " text-align:left;" + " font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: normal;" + " width:99%;";
      crearEstiloConSelIgual("input.cusRO", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px; outline:none;" + 
        agregarFondo(21) + " width:99%;";
      crearEstilo("input.cusck", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px; outline:none;" + 
        agregarFondo(3) + " width:99%;";
      crearEstilo("input.cusckSEL", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(8) + " width:99%;";
      crearEstiloConSelIgual("input.cusckRO", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(21) + " text-align:left;" + " font-family: Arial, Helvetica, sans-serif; font-size: 10px; font-weight: normal;" + " width:99%;";
      crearEstilo("select.cussl", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(3) + " text-align:left;" + " font-family: Arial, Helvetica, sans-serif; font-size: 10px; font-weight: normal;" + " width:99%;";
      crearEstilo("select.cusslSEL", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(8) + " text-align:left;" + " font-family: Arial, Helvetica, sans-serif; font-size: 10px; font-weight: normal;" + " width:99%;";
      crearEstiloConSelIgual("select.cusslRO", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(21) + " text-align:left;" + " width:99%;";
      crearEstilo("input.come", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(3) + " text-align:left;" + " width:99%;";
      crearEstilo("input.comeSEL", contenido);
      contenido = " margin: 0px 0px 0px 0px; padding: 0px 0px 0px  0px; border-style: outset; border-width: 0px 0px 0px 0px;" + 
        agregarFondo(8) + " text-align:left;" + " width:99%;";
      crearEstiloConSelIgual("input.comeRO", contenido);
      for (int i = 0; i < lines.size(); i++)
        out.println(lines.get(i)); 
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
}
