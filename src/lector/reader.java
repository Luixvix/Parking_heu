package lector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class reader {

	public static void main(String[] args) {
		reader r = new reader();
		Path inputFile = Paths.get(args[0]);
		Path outputFile = Paths.get(inputFile + ".output");

		try {
			String[][] map = r.fileReader(inputFile);
			r.closeFile(outputFile, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Nombre: fileReader Entrada: Path filename, contiene el nombre/direccion
	 * del archivo a convertir Salida: char[][], devuelve un array 2D con los
	 * valores leidos del archivo Funcion: lee el contenido en txt del fichero
	 * que contiene el mapa y lo parsea a un array 2D para poder moverse mejor.
	 */
	public String[][] fileReader(Path filename) throws IOException {
		List<String> file = Files.readAllLines(filename);
		int y = file.get(0).charAt(0) - '0';
		int x = file.get(0).charAt(1) - '0';
		String[][] map = new String[y][x];
		for (int i = 0; i < y; i++) {
			int k = 0;
			for (int j = 0; j < x; j++) {
				String tmp = "" + file.get(i+1).charAt(k) + file.get(i+1).charAt(k + 1);
				k += 2;
				map[i][j] = tmp;
			}
		}
		/*
		for (int j = 0; j < y; j++) {
			for (int j2 = 0; j2 < x; j2++) {
				System.out.print(map[j][j2] + " ");
			}
			System.out.println();
		}
		*/
		return map;
	}

	/*
	 * Nombre: closeFile Funcion: escribe en el fichero de destino el mapa
	 * proporcionado como una matriz de bytes
	 * 
	 * Salida: ninguna
	 * 
	 * @param filename
	 * 
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
