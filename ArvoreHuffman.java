package arvorehuffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

class no{
    String valor;
    String codigoHuffman;
    int frequencia;
    no esq;
    no dir;
    no linker;
    no linkerBack;

    no(String valor, int frequencia) {
            this.valor = valor;
            this.frequencia = frequencia;
            this.codigoHuffman = "";
            this.esq = null;
            this.dir = null;
            this.linker = null;
            this.linkerBack = null;
            
    }
}

public class ArvoreHuffman{
    static no no;
    static int k = 0;
    
    //Funçao para montar arvore binária e imprimir 
    static void geraArvore(no raiz, no fim){
        no = new no(fim.linkerBack.valor + fim.valor, fim.linkerBack.frequencia + fim.frequencia);
        no.esq = fim.linkerBack;
        no.dir = fim;
        fim.linkerBack.linkerBack.linker = no;
        no.linkerBack = fim.linkerBack.linkerBack;
        fim = no;
        fim.linker = null;
        no n = raiz;

        while(n.linker != null) {
            System.out.print(n.valor + "->");
            n = n.linker;
        }

        System.out.println(n.valor);

        if(raiz.linker == fim) { //se a arvore estiver vazia
            no = new no(raiz.valor + fim.valor, raiz.frequencia + fim.frequencia);
            no.esq = raiz;
            no.dir = fim;
            no.linker = null;
            no.linkerBack = null;
            System.out.println(no.valor);
        } else { 
            geraArvore(raiz, fim); 
        }
    }
    
    /* 3. Codificação dos caracteres com base na árvore de Huffman. */
    static void geraCodigos(char input, no raiz, String codigo, no[] verifica, boolean repetido){
        no n = raiz;
        repetido = false;
        
        //verificar se char já foi usado;
        for(int j = 0; j < k; j++){
            if(n.valor.equals(verifica[j].valor)){
                repetido = true;
            }
        }
        
       if(!repetido){
            if(n.esq == null && n.dir == null){
                if(n.valor.toCharArray()[0] == input){
                    n.codigoHuffman = codigo;
                    verifica[k] = n;
                    System.out.println("Caractere: " + verifica[k].valor + " Frequência: " + verifica[k].frequencia + " Código Huffman: " + verifica[k].codigoHuffman);
                    k++;
                }
            } else {
                geraCodigos(input, n.esq, codigo + "0", verifica, repetido);
                geraCodigos(input, n.dir, codigo + "1", verifica, repetido);
            }        
        }         
    }
        
    static String codificador(no raiz, String input){
        char[] messageArray = input.toCharArray();
        String codificada = "";     

        for(int i = 0; i < messageArray.length; i++) {
                no n = raiz;
                String code = "";
                
                if(messageArray[i] == ' '){
                     messageArray[i] = '_';
                }
                
                while(true){
                    if(n.esq != null){
                        if(n.esq.valor.toCharArray()[0] == messageArray[i]){
                            code += n.esq.codigoHuffman;
                            break;
                        } else {
                            code += n.codigoHuffman;
                            if(n.dir != null) {
                                n = n.dir;
                            } else { break;}                                
                        }
                    } else { 
                        if(n.dir == null){
                            code += n.codigoHuffman;
                            break;
                        }
                        break; }
                }
            codificada += code;          
        }
        return codificada;
    }
    
    static String decodificador(String code){
        String decod = "";
        no n = no;

        for(int i = 0; i < code.length(); i++){
            boolean checa = false;
            
            if(Character.getNumericValue(code.charAt(i)) == 0){
                if(n.esq == null){
                    checa = true;
                    i--;
                } else {
                    n = n.esq;
                    if(n.esq == null && n.dir == null) checa = true;
                }
            }
            
            if(Character.getNumericValue(code.charAt(i)) == 1){
                if(n.dir == null){
                    checa = true;
                    i--;
                } else { 
                    n = n.dir;
                    if(n.esq == null && n.dir == null) 
                        checa = true;
                }
            }
            
            if(checa){
                decod += n.valor;
                n = no;
            }
        }
        return decod;
    }
 
    public static void main(String[] args) throws IOException {
        //ler texto de entrada
        File file = new File("C://entrada.txt");
        byte[] data;
        try (FileInputStream fis = new FileInputStream(file)) {
            data = new byte[(int) file.length()];
            fis.read(data);
        }
        
        String input = new String(data);
        char[] msgChar = input.toCharArray();
        ArrayList<Character> chars = new ArrayList<>();
        
        System.out.println("\nEntrada: " + input);

        /* 1. Calculo da frequência de ocorrência de cada caracter no arquivo; */
        for (int i = 0; i < msgChar.length; i++) {
            if(msgChar[i] == ' '){
                msgChar[i] = '_';
            }

            if (!(chars.contains(msgChar[i]))) {
                chars.add(msgChar[i]);
            }
        }

        int[] contador = new int[chars.size()];

        for (int j = 0; j < contador.length; j++) {
            contador[j] = 0;
        }

        for (int i = 0; i < chars.size(); i++) {
            char checa = chars.get(i);
            for (int j = 0; j < msgChar.length; j++) {
                if (checa == msgChar[j]) {
                    contador[i]++;
                }
            }
        }
        for (int i = 0; i < contador.length -1; i++){
            for (int j = 0; j < contador.length - 1; j++) {
                if (contador[j] < contador[j + 1]) {
                    int aux = contador[j];
                    contador[j] = contador[j + 1];
                    contador[j + 1] = aux;
                    
                    char auxChar = chars.get(j);
                    chars.set(j, chars.get(j + 1));
                    chars.set(j + 1, auxChar);
                }
            }
        }
            
        /** 2. Construção de uma árvore binária **/
        no raiz = null;
        no fim = null;
        no n;

        for(int i = 0; i < contador.length; i++) {
            no aux = new no(chars.get(i).toString(), contador[i]);
            if (raiz == null){
                raiz = aux;
                fim = aux;
            } else {
                n = raiz;
                while (n.linker != null){
                        n = n.linker;
                }
                n.linker = aux;
                n.linker.linkerBack = n;
                fim = aux;
            }
        }
        
        System.out.println("\nÁrvore binária: ");
        geraArvore(raiz, fim);   
              
        /** 4. Exibir caracteres com seus respectivos codigos **/
        no[] verifica = new no[chars.size()];
        System.out.println("");
        
        for(int i = 0; i < msgChar.length; i++){
            geraCodigos(msgChar[i], no, "", verifica, false);
        }
        
        System.out.println("\nEntrada codificada: " + codificador(no, input));

        System.out.println("Decodificando...\nEntrada decodificada: " + decodificador(codificador(no,input))); 
        
    }
} 

