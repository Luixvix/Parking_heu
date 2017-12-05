package parking;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jacop.core.BooleanVar;
import org.jacop.core.Store;
import org.jacop.jasat.utils.structures.IntVec;
import org.jacop.satwrapper.SatWrapper;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

import parking.coche;
import lector.reader;

public class SATParking {

	public static void main(String[] args) {

		Boolean result = false;
		Store store = new Store();
		SatWrapper satWrapper = new SatWrapper();
		store.impose(satWrapper);

		reader r = new reader();
		Path inputFile = Paths.get(args[0]);
		Path outputFile = Paths.get(inputFile + ".output");

		try {
			// map[y][x] con los valores del fichero
			String[][] map = r.fileReader(inputFile);
			int calle_y = map.length;
			int plaza_x = map[0].length;

			// Lista de variables booleanVar para el jacop
			List<BooleanVar> listaVehiculos = new ArrayList<BooleanVar>();
			// Lista de las variables coche, que contienen su categoria (de
			// forma numerica), orden de llegada, calle en la que se encuentran
			// y
			// el numero de plaza
			List<coche> coches = new ArrayList<coche>();

			for (int i = 0; i < calle_y; i++) {
				for (int j = 0; j < calle_y; j++) {
					String tmp = map[i][j];
					int categoria = 0;
					switch (tmp) {
					case "__":

						break;
					default:
						char ctmp = tmp.charAt(0);
						int llegada = tmp.charAt(1) - '0';
						switch (ctmp) {
						case 'A':
							categoria = 1;
							break;
						case 'B':
							categoria = 2;
							break;
						case 'C':
							categoria = 2;
							break;
						}
						coche c = new coche(categoria, llegada, i, j);
						BooleanVar cb = new BooleanVar(store, "Coche, Cat: " + categoria + " Orden: " + llegada
								+ " Calle: " + i + " Plaza: " + j + "");
						listaVehiculos.add(cb);
						coches.add(c);
						break;
					}
				}
			}
			BooleanVar array_vehiculos [] = new BooleanVar [listaVehiculos.size()];
			for (int i = 0; i < listaVehiculos.size(); i++) {
				array_vehiculos[i] = listaVehiculos.get(i);
				satWrapper.register(array_vehiculos[i]);
			}
			
			
			/*
			 * for (int i = 0; i < coches.size(); i++) {
			 * System.out.println("Coche: "+ i);
			 * System.out.println(coches.get(i).getCategoria());
			 * System.out.println(coches.get(i).getLlegada());
			 * System.out.println(coches.get(i).getCalle());
			 * System.out.println(coches.get(i).getN_plaza());
			 * 
			 * }
			 * 
			 */

			/*
			 * 
			 * REGLAS
			 * 
			 */

			r.closeFile(outputFile, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
