
package lab5.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class AdminComandos {
    File fileActual=new File("C:\\\\Program Files\\\\Java");
    
    public void Mkdir(String entrada){
        String nombre=entrada.substring(6);
        File fileNuevo=new File(fileActual, nombre);
        fileNuevo.mkdir();
    }
    
    public void Mfile(String entrada) throws IOException{
        String nombre=entrada.substring(6);
        File fileNuevo=new File(fileActual, nombre);
        fileNuevo.createNewFile();
    }
    
    public void Rm(String entrada){
        String nombre=entrada.substring(3);
        File fileNuevo=new File(fileActual, nombre);
        if(fileNuevo.isFile())
            fileNuevo.delete();
        else{
            if(fileNuevo.length()==0)
                fileNuevo.delete();
            else{
               borrarCarpetas(fileNuevo.listFiles().length, fileNuevo.listFiles());
               fileNuevo.delete();
                }
        }
    }
    
    private void borrarCarpetas(int contador, File[] files){
        if(contador >= 0){
            if(files[contador].isDirectory() && files[contador].listFiles().length!=0){
                    borrarCarpetas(files[contador].listFiles().length - 1, files[contador].listFiles());
            }
            files[contador].delete();
            borrarCarpetas(contador - 1, files);
        }
    }
    
    public void Cd(String entrada){
        String nombre=entrada.substring(3);
        File fileNuevo=new File(fileActual, nombre);
        if(fileNuevo.exists() && fileNuevo.isDirectory())
            fileActual=fileNuevo;
    }
    
    public void regresarCarpeta(){
        if(fileActual.getParentFile()!=null)
            fileActual=fileActual.getParentFile();
    }
    
    public void Dir(){
        for(File f: fileActual.listFiles())
          System.out.println(f.getName());
    }
    
    public void Date(){
        System.out.println(LocalDate.now());
    }
    
    public void Time(){
        System.out.println(LocalTime.now());
    }
    
    public void Escribir(String entrada) throws IOException{
        Scanner n=new Scanner(System.in);
        String nombre=entrada.substring(3);
        File fileNuevo=new File(fileActual, nombre);
        
        if(fileNuevo.exists()){
            BufferedWriter escritor = new BufferedWriter(new FileWriter(fileNuevo, true));
            String texto;
            do{
                texto=n.nextLine();
                if(!texto.equalsIgnoreCase("EXIT")){
                    escritor.write(texto);
                    escritor.newLine();
                }
            }while(!texto.equalsIgnoreCase("exit"));
        }
    }
    
    public void Leer(String entrada)throws IOException{
        String nombre= entrada.substring(3);
        File fileNuevo=new File(fileActual, nombre);
        
        if(fileNuevo.exists()){
            BufferedReader lector= new BufferedReader(new FileReader(fileNuevo));
            String linea;
            while(lector.readLine()!=null){
                linea=lector.readLine();
                System.out.println(linea);
            }
        }
    }
}
    
