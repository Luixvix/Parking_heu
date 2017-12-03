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
	public String[][] fileReader(Path filename) throws IOException {
		List<String> file = Files.readAllLines(filename);
		int y = (int)(file.get(0).charAt(0));
		int  x = (int)(file.get(0).charAt(1));
		String[][] map = new String[x][y];
		for (int i = 1; i < y; i++) {
			for (int j = 0; j < 2*x; j+=2) {
				String tmp = ""+ file.get(i).charAt(j) + file.get(i).charAt(j+1);
				map[i][j] = tmp;
			}
		}
		return map;
	}
	/*
	 *  Nombre: closeFile
	 *  Funcion: escribe en el fichero de destino el mapa proporcionado
	 *  como una matriz de bytes
	 *  
	 *  Salida: ninguna
	 *  
	 * @param filename
	 * @param map
	 */
	public void closeFile(Path filename, String[][] map) {
		try {
			FileWriter flS = new FileWriter(filename.toFile());
			BufferedWriter fS = new BufferedWriter(flS);
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					fS.write(map[i][j]);
				}
				fS.newLine();
			}
			fS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
