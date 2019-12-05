package compilador;
import java.io.*;
import java.lang.*;
public class Compilador {
    public static int[][] Transicion = 
    {
    /*0*/ {  1,  2,104,105,106,  6,  5,  9,110, 10,120,121,122,  0,  0,  0,  0,503},
    /*1*/ {  1,  1,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100},
    /*2*/ {102,  2,102,102,102,102,102,102,102,102,102,102,  3,102,102,102,102,102},
    /*3*/ {500,  4,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500},
    /*4*/ {103,  4,103,103,103,103,103,103,103,103,103,103,103,103,103,103,103,103},
    /*5*/ {  5,  5,  5,  5,  5,  5,101,  5,  5,  5,  5,  5,  5,501,  5,  5,  5,  5},
    /*6*/ {107,107,107,107,  7,107,107,107,107,107,107,107,107,107,107,107,107,107},
    /*7*/ {  7,  7,  7,  7,  8,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,  7,505,  7,  7},
    /*8*/ {506,506,506,506,  7,  0,506,506,506,506,506,506,506,506,506,506,506,506,506},
    /*9*/ {108,108,108,108,108,108,108,108,109,114,108,108,108,108,108,108,108,108,108},
    /*10*/{112,112,112,112,112,112,112,112,111,112,112,112,112,112,112,112,112,112,112}
    };
    public static int columna;
    public static int estado = 0;
    public static boolean ayuda = false;
    public static String[][] Reservadas = 
    {
        {"200","and"},
        {"201","or"},
        {"202","not"},
        {"203","true"},
        {"204","false"},
        {"205","if"},
        {"206","then"},
        {"207","else"},
        {"208","while"},
        {"209","do"},
        {"210","end"},
        {"211","repeat"},
        {"212","until"},
        {"213","loop"},
        {"214","forever"},
        {"215","go"},
        {"216","to"},
        {"217","exit"},
        {"218","case"},
        {"219","procedure"},
        {"220","return"},
        {"221","call"},
        {"222","read"},
        {"223","print"},
        {"224","by"},
        {"225","cycle"},
        {"226","endcase"},
        {"227","continue"},
        {"228","program"},
        {"229","var"},
    };
    public static String[][] Errores = 
    {
        {"500","Se espera digito"},
        {"501","End of Line o Salto de Linea inesperado"},
        {"502","Se esperaba el simbolo '='"},
        {"503","Caracter no conocido"},
        {"504","Se esperaba el simbolo ':"},
        {"505","EOF inesperado"},
        {"506","Se esperaba comentario"}
    };

    public static int ValorMT = 0;
    public static String LexemaReal = "";
    public static Nodo Cabeza;
    public static Nodo P;
    public static Nodo AUX;
    public static int Renglon = 1;
    public static String ruta = "C:\\Users\\maram\\OneDrive\\Escritorio\\La ultima version del universon\\CompiSpark\\textito.txt";
    public static String archivo = "";
    public static boolean encontrado = false;
    public static char caracter;
    public static int x;
    
    public static void main(String[] args) throws IOException  {
        Leer();
        //System.out.println(""+archivo);
        
        for(x = 0; x < archivo.length(); x++){
            caracter = archivo.charAt(x);
            int asciiValue = (int)caracter;
            if(Character.isLetter(caracter)){
                columna = 0;
                ayuda=true;
            }else if(Character.isDigit(caracter))
            {
                columna = 1;
                ayuda=true;
            }
            else if(asciiValue == 3){
                columna = 13;
                ValorMT = 3;
            }
            else if(asciiValue == 32){
                columna = 14;
                ValorMT = 32;
            }
            else if(asciiValue == 10){
                columna = 15;
                ValorMT = 10;
            }
            else if(asciiValue == 9){
                columna = 16;
                ValorMT = 9;
            }
            else
                switch(caracter){
                    case '+': columna = 2; break;
                    case '-': columna = 3; break;
                    case '*': columna = 4; break;
                    case '/': columna = 5; break;
                    case '"': columna = 6; ayuda=false; break;
                    case '<': columna = 7; break;
                    case '=': columna = 8; break;
                    case '>': columna = 9; break;
                    case ':': columna = 10; break;
                    case ';': columna = 11; break;
                    case '.': columna = 12; break;
                    default: columna = 17; break; //Otra Cosa
                }
            Realizar();
            }Consulta();
            Sintactico();
        }
        
   // }
    public static void Realizar() throws IOException{
        if(ValorMT == 0){
            LexemaReal = "";
        }
        ValorMT = Transicion[estado][columna];
        
            if(ValorMT < 100){
                estado = ValorMT;
                LexemaReal = LexemaReal + caracter;
            } 
            if(ValorMT == 100){
                ValorMT = BuscarPalabraReservada(ValorMT,LexemaReal);
                Validaciones();
            }
            if(ValorMT >= 100 && ValorMT < 500){
                Validaciones();
                insertarNodo();
                estado = 0;
                LexemaReal = "";
                if(columna == 17){
                    x--;    
                }
                if(columna == 15){
                    Renglon++;
                }
            }if(ValorMT >= 500){
                String Error = DameElError(ValorMT);
                System.out.println(""+ValorMT+" | "+Error);
            }
    }
    public static void Validaciones() throws IOException{
        if(ValorMT == 101 || ValorMT == 114){
            LexemaReal = LexemaReal + caracter;
        }
        if(columna==2){
            if(ayuda==true){
                insertarNodo();
                ayuda=false;
            }
            LexemaReal = "+";
            ValorMT = 104;
        }
        if(columna==3){
            if(ayuda==true){
                insertarNodo();
                ayuda=false;
            }
            LexemaReal = "-";
            ValorMT = 105;
        }
        if(columna==4){
            if(ayuda==true){
                insertarNodo();
                ayuda=false;
            }
            LexemaReal = "*";
            ValorMT = 106;
        }
        if(columna==10){
            if(ayuda==true){
                insertarNodo();
                ayuda=false;
            }
            LexemaReal = ":";
            ValorMT = 120;
        }
        if(columna==5){
            if(ayuda==true){
                insertarNodo();
                ayuda=false;
            }
            LexemaReal = "/";
            ValorMT = 107;
        }
        if(columna==11){
            if(ayuda==true){
                insertarNodo();
                ayuda=false;
            }
            LexemaReal = ";";
            ValorMT = 121;
        }
        if(columna==12){
            if(ayuda==true){
                insertarNodo();
                ayuda=false;
            }
            LexemaReal = ".";
            ValorMT = 122;
        }
        if(columna==8){
            boolean antes=false;
            if(ayuda==true){
                insertarNodo();
                ayuda=false;
            }
            if(ValorMT==109){
                antes=true;
                LexemaReal = "<="; 
            }
            if(ValorMT==111){
                antes=true;
                LexemaReal= ">=";
            }
            if(antes==false){
                LexemaReal = "=";
                ValorMT = 110;
            }
        }
    }
    public static void Leer(){
        FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(ruta);
			bufferedReader = new BufferedReader(fileReader);
			StringBuilder stringBuilder = new StringBuilder("");
			String linea;
			while ((linea = bufferedReader.readLine()) != null) {
				// Lee línea por línea, omitiendo los saltos de línea
				stringBuilder.append(linea + "\n");
                                
			}
                        archivo = stringBuilder.toString();
			//System.out.println("Contenido: " + stringBuilder.toString());

		} catch (IOException e) {
			System.out.println("Excepción leyendo archivo: " + e.getMessage());
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (IOException e) {
				System.out.println("Excepción cerrando: " + e.getMessage());
			}
		}
    }
    public static int BuscarPalabraReservada(int xValor,String xLexema){
        for(int filas = 0; filas < Reservadas.length; filas++){
            for(int columnitas = 0; columnitas < Reservadas[filas].length; columnitas++){
                if(Reservadas[filas][1].equals(xLexema)){
                    xValor = Integer.parseInt(Reservadas[filas][0]);
                    return xValor;
                }
            }
        }
        return xValor;
    }
    public static String DameElError(int xValor){
        String xError = "";
        String xValor2 = Integer.toString(xValor);
        for(int filas = 0; filas < Errores.length; filas++){
            for(int columnitas = 0; columnitas < Errores[filas].length; columnitas++){
                if(Errores[filas][0].equals(xValor2)){
                    xError = Errores[filas][1];
                    return xError;
                }
            }
        }
        return xError;
    }
    public static void insertarNodo() throws IOException{
        if(Cabeza == null){
            Cabeza = new Nodo();
            Cabeza.Lexema = LexemaReal;
            Cabeza.Token = ValorMT;
            Cabeza.renglon = Renglon;
            Cabeza.Sig = null;
            P = Cabeza;
        }else{
            AUX = new Nodo();
            AUX.Lexema = LexemaReal;
            AUX.Token = ValorMT;
            AUX.renglon = Renglon;
            AUX.Sig = null;
            P.Sig = AUX;
            P = AUX;
        }
    }
    
    
    
    public static void Consulta() throws NullPointerException{
        if(Cabeza == null){
            System.out.println("Lista vacia");
        }
        AUX = Cabeza;
        
        System.out.println("|   Lexema    |   Token   |   Renglon    |" );
        do {
            System.out.print("|   " + AUX.Lexema + "  |");
            System.out.print("   " + AUX.Token + "   |");
            System.out.print("   " + AUX.renglon + "   |");
            System.out.println(""); 
            AUX = AUX.Sig;
        }while(AUX != null);
    }
    
    public static void Sintactico() throws NullPointerException{
         if(Cabeza == null){
            System.out.println("Lista vacia");
        }
         
        AUX = Cabeza;
        do {
            if (AUX.Token == 228) {//program
                AUX = AUX.Sig;

                if (AUX.Token == 100) {//nombre id
                    AUX = AUX.Sig;

                    if (AUX.Token == 121) {//;
                        AUX = AUX.Sig;
//BLOCK
                        if (AUX.Token == 229) {//var
                            AUX = AUX.Sig;

                            if (AUX.Token == 100) {//nombre var
                                AUX = AUX.Sig;

                                if (AUX.Token == 120) {//:
                                    AUX = AUX.Sig;

                                    if (AUX.Token == 100) {//tipos de dato
                                        AUX = AUX.Sig;

                                        if (AUX.Token == 121) {//;
                                            AUX = AUX.Sig;
//statement if
                                            if (AUX.Token == 205) {//if
                                                AUX = AUX.Sig;

                                                if (AUX.Token == 102) {//expresion del if,solo acepta digito
                                                    AUX = AUX.Sig;

                                                    if (AUX.Token == 100) {//dentro del if
                                                        AUX = AUX.Sig;

                                                        if (AUX.Token == 206) {//then
                                                            AUX = AUX.Sig;

                                                            if (AUX.Token == 100) {//dentro del then
                                                                AUX = AUX.Sig;

                                                                if (AUX.Token == 207) {//else
                                                                    AUX = AUX.Sig;

                                                                    if (AUX.Token == 100) {//dentro del else
                                                                        AUX = AUX.Sig;

                                                                        if (AUX.Token == 121) {//fin del else;;;
                                                                            AUX = AUX.Sig;
//statement while
                                                                            if (AUX.Token == 208) {//while
                                                                                AUX = AUX.Sig;

                                                                                if (AUX.Token == 100) {//dentro del while
                                                                                    AUX = AUX.Sig;

                                                                                    if (AUX.Token == 209) {//do
                                                                                        AUX = AUX.Sig;

                                                                                        if (AUX.Token == 100) {//do;
                                                                                            AUX = AUX.Sig;

                                                                                            if (AUX.Token == 121) {//do;
                                                                                                AUX = AUX.Sig;

                                                                                                //end y el punto
                                                                                                if (AUX.Token == 210) {
                                                                                                    AUX = AUX.Sig;

                                                                                                    if (AUX.Token == 122) {
                                                                                                        AUX = AUX.Sig;
                                                                                                        //end y el punto

                                                                                                    } else if (AUX.Sig.Token != (122 | 3 | 32 | 10 | 9)) {
                                                                                                        System.out.println("Se espera un '.'");
                                                                                                        System.exit(0);
                                                                                                    }

                                                                                                } else if (AUX.Sig.Token != (210 | 3 | 32 | 10 | 9)) {
                                                                                                    System.out.println("Se espera la palabra 'end'");
                                                                                                    System.exit(0);
                                                                                                }
                                                                                            } else if (AUX.Sig.Token != (121 | 3 | 32 | 10 | 9)) {
                                                                                                System.out.println("Se espera un ';'");
                                                                                                System.exit(0);
                                                                                            }
                                                                                        } else if (AUX.Sig.Token != (100 | 3 | 32 | 10 | 9)) {
                                                                                            System.out.println("Se espera una palabra");
                                                                                            System.exit(0);
                                                                                        }

                                                                                    } else if (AUX.Sig.Token != (209 | 3 | 32 | 10 | 9)) {
                                                                                        System.out.println("Se espera la palabra 'do'");
                                                                                        System.exit(0);
                                                                                    }
                                                                                } else if (AUX.Sig.Token != (100 | 3 | 32 | 10 | 9)) {
                                                                                    System.out.println("Se espera una palabra");
                                                                                    System.exit(0);
                                                                                }
                                                                            } else if (AUX.Sig.Token != (208 | 3 | 32 | 10 | 9)) {
                                                                                System.out.println("Se espera la palabra 'while'");
                                                                                System.exit(0);
                                                                            }
                                                                        } else if (AUX.Sig.Token != (121 | 3 | 32 | 10 | 9)) {
                                                                            System.out.println("Se espera un ';'");
                                                                            System.exit(0);
                                                                        }
                                                                    } else if (AUX.Sig.Token != (100 | 3 | 32 | 10 | 9)) {
                                                                        System.out.println("Se espera una palabra");
                                                                        System.exit(0);
                                                                    }
                                                                } else if (AUX.Sig.Token != (207 | 3 | 32 | 10 | 9)) {
                                                                    System.out.println("Se espera la palabra 'else'");
                                                                    System.exit(0);
                                                                }
                                                            } else if (AUX.Sig.Token != (100 | 3 | 32 | 10 | 9)) {
                                                                System.out.println("Se espera una palabra");
                                                                System.exit(0);
                                                            }
                                                        } else if (AUX.Sig.Token != (206 | 3 | 32 | 10 | 9)) {
                                                            System.out.println("Se espera la palabra 'then'");
                                                            System.exit(0);
                                                        }
                                                    } else if (AUX.Sig.Token != (100 | 3 | 32 | 10 | 9)) {
                                                        System.out.println("Se espera una palabra");
                                                        System.exit(0);
                                                    }
                                                } else if (AUX.Sig.Token != (102 | 3 | 32 | 10 | 9)) {
                                                    System.out.println("Se espera un digito" + AUX.Lexema);
                                                    System.exit(0);
                                                }
                                            } else if (AUX.Sig.Token != (210 | 3 | 32 | 10 | 9)) {
                                                System.out.println("Se espera la palabra 'if'");
                                                System.exit(0);
                                            }

                                        } else if (AUX.Sig.Token != (121 | 3 | 32 | 10 | 9)) {
                                            System.out.println("Se espera un ';'");
                                            System.exit(0);
                                        }
                                    } else if (AUX.Sig.Token != (100 | 3 | 32 | 10 | 9)) {//erro de tipos de dato
                                        System.out.println("Se espera el tipo de dato" + AUX.Lexema);
                                        System.exit(0);
                                    }
                                } else if (AUX.Sig.Token != (120 | 3 | 32 | 10 | 9)) {
                                    System.out.println("Se espera un ':'");
                                    System.exit(0);
                                }
                            } else if (AUX.Sig.Token != (100 | 3 | 32 | 10 | 9)) {
                                System.out.println("Se espera una palabra");
                                System.exit(0);
                            }

                        } else if (AUX.Sig.Token != (229 | 3 | 32 | 10 | 9)) {
                            System.out.println("Se espera la palabra 'var'");
                            System.exit(0);
                        }

                    } else if (AUX.Sig.Token != (121 | 3 | 32 | 10 | 9)) {
                        System.out.println("Se espera un ';'");
                        System.exit(0);
                    }
                } else if (AUX.Sig.Token != (100 | 3 | 32 | 10 | 9)) {
                    System.out.println("Se espera una palabra");
                    System.exit(0);
                }
            } else if (AUX.Sig.Token != (100 | 228 | 3 | 32 | 10 | 9)) {//| 100 | 121 | 3 | 32 | 10 | 9
                System.out.println("Se espera la palabra 'program'");
                System.exit(0);
            }
        }while(AUX != null);
    }       
}
