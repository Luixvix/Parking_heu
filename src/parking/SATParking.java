package parking;

import java.io.IOException;
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

public class SATParking {

	public static void main(String[] args) {

		Boolean result = false;
		Store store = new Store();
		SatWrapper satWrapper = new SatWrapper();
		store.impose(satWrapper);

		try {
			// Lista de variables booleanVar para el jacop
			List<BooleanVar> listaCoches = new ArrayList<BooleanVar>();// nos sirve para crear una variable boolean de
																		// decision sobre los coches que luego usara el
																		// jacob
			// Lista de las variables coche, que contienen su categoria (de forma numerica),
			// orden de llegada, calle en la que se encuentran y
			// el numero de plaza
			List<coche> coches = new ArrayList<coche>(); // nos servira para operar con los coches

			/*
			 * 
			 * REGLAS
			 * 
			 */

			// Creamos una variable de decisión para cada coche de la lista de tipo coche
			for (int i = 0; i < coches.size(); i++) {
				BooleanVar x = new BooleanVar(store, "Coche de categoria: "+ coches.get(i).getCategoria() +" con llegada: "+ coches.get(i).getLlegada()+" en la calle: "+ coches.get(i).getCalle()+" en la plaza: "+ coches.get(i).getN_plaza());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
