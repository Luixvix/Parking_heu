package lector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class reader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/*
	 * Nombre: fileReader
	 * Entrada: Path filename, contiene el nombre/direccion del archivo a
	 * convertir
	 * Salida: char[][], devuelve un array 2D con los valores leidos del archivo
	 * Funcion: lee el contenido en txt del fichero que contiene el mapa y lo
	 * parsea a un array 2D para poder moverse mejor.
	 */
	public byte[][] fileReader(Path filename) throws IOException {
		List<String> file = Files.readAllLines(filename);
		int y = file.get(0).length();
		int  x = file.size();
		byte[][] map = new byte[x][y];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j] = (byte) file.get(i).charAt(j);
			}
		}
		return map;
	}
	/*
	 *  Nombre: ExitTheMatrix
	 *  Funcion: escribe en el fichero de destino el mapa proporcionado
	 *  como una matriz de bytes
	 *  
	 *  Salida: ninguna
	 *  
	 * @param filename
	 * @param map
	 */
	public void ExitTheMatrix(Path filename, byte[][] map) {
		try {
			FileWriter flS = new FileWriter(filename.toFile());
			BufferedWriter fS = new BufferedWriter(flS);
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					fS.write((char)map[i][j]);
				}
				fS.newLine();
			}
			fS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
