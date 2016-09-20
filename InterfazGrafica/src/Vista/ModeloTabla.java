/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Alejandro
 */
public class ModeloTabla extends AbstractTableModel  {
    private File[] archivos;
    private FileSystemView vistaSistema = FileSystemView.getFileSystemView();
    private String[] columnas = {
        "Tipo",        
        "Nombre",
        "Ruta",
        "Tamaño",
        "Modificacion",
    };
   ModeloTabla() {
        this(new File[0]);
    }

    ModeloTabla(File[] archivos) {
        this.archivos = archivos;
    }

    @Override
    public Object getValueAt(int row, int column) {
        File file = archivos[row];
        switch (column) {
            case 0:
                return vistaSistema.getSystemIcon(file);
            case 1:
                return vistaSistema.getSystemDisplayName(file);
            case 2:
                return file.getPath();
            case 3:
                return file.length()+" bytes";
            case 4:
                return file.lastModified();          
            default:
                System.err.println("Logic Error");
        }
        return "";
    }
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImageIcon.class;
            case 3:
                return Long.class;
            case 4:
                return Date.class;
        }
        return String.class;
    }
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
    @Override
    public int getRowCount() {
        return archivos.length;
    }

    public void setFiles(File[] files) {
        this.archivos = files;
        fireTableDataChanged();
    }
    
}
