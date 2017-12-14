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
			List<BooleanVar> listaVehiculos = new ArrayList<BooleanVar>();// nos sirve para crear una variable boolean
																			// de
																			// decision sobre los coches que luego usara
																			// el
																			// jacob
			// Lista de las variables coche, que contienen su categoria (de forma numerica),
			// orden de llegada, calle en la que se encuentran y
			// el numero de plaza
			List<coche> coches = new ArrayList<coche>(); // nos servira para operar con los coches

			// Falta lista coches Literales

			int totalVariables = listaVehiculos.size();
			int totalCoches = coches.size();
			// int totalPacman = listaLiterales.size();

			/*
			 * for // CREACION DE BOOLEANVAR // BooleanVar x = new BooleanVar(store,
			 * "Hay un agente de seguridad en el nodo x"); // VAMOS GUARDANDO ESTAS
			 * VARIABLES EN LISTAVEHICULOS
			 * 
			 * // REGISTRO DE BOOLEANVAR //satWrapper.register(x);
			 * 
			 * // CREACION DE LITERALES //int xLiteral = satWrapper.cpVarToBoolVar(x, 1,
			 * true);
			 */

			// LAS POSICIONES DE LOS COCHES LAS PUEDO HACER CON NUM_PLAZA?

			// Regla 1
			// Comprobamos la categoria del coche de adelante y el de atras
			for (int i = 0; i < totalCoches; i++) {

				if (i + 1 > totalCoches) {
					break;
					// salimos del bucle cuando i+1 se salga fuera del num de coches

				}
				// si los coches estan en la misma calle
				if (coches.get(i).getCalle() == coches.get(i + 1).getCalle()) {
					// si el coche de delante tiene mas categoría
					if (coches.get(i).getCategoria() < coches.get(i + 1).getCategoria()) {
						IntVec bloqueoDelante = new IntVec(satWrapper.pool);
						// añadimos los literales a la clausula de bloqueo delante
						bloqueoDelante.add(listaLiterales.get(i).getLiteral());
					}
					// si el coche de detras tiene mas categoría que nosotros
					// lo hemos indicado para que compruebe si el siguiente coche en la lista tiene
					// menos categoria que el actual
					if (coches.get(i + 1).getCategoria() < coches.get(i).getCategoria()) {
						IntVec bloqueoDetras = new IntVec(satWrapper.pool);
						// añadimos los literales a la clausula de bloqueo delante
						bloqueoDetras.add(listaLiterales.get(i).getLiteral());
					}
				}

			}

			// Regla 2
			// Comprobamos la llegada del coche de adelante y el de atras

			for (int i = 0; i < totalCoches; i++) {

				if (i + 1 > totalCoches) {
					break;
					// salimos del bucle cuando i+1 se salga fuera del num de coches

				}
				// si los coches estan en la misma calle
				if (coches.get(i).getCalle() == coches.get(i + 1).getCalle()) {
					//si son de la misma categoria
					if (coches.get(i).getCategoria() == coches.get(i + 1).getCategoria()) {
						// si el coche de delante llego despues
						if (coches.get(i).getLlegada() < coches.get(i + 1).getCategoria()) {
							IntVec bloqueoDelante = new IntVec(satWrapper.pool);
							// añadimos los literales a la clausula de bloqueo delante
							bloqueoDelante.add(listaLiterales.get(i+1).getLiteral());
							satWrapper.addModelClause(bloqueoDelante.toArray());// añadimos la clausula al satWrapper
						}
						// si el coche de detras llego despues
						// lo hemos indicado para que compruebe si el siguiente coche en la lista llego antes que nosotros
						// es ese caso el coche de delante esta bloqueado por detras por nosotros
						if (coches.get(i + 1).getLlegada() < coches.get(i).getLlegada()) {
							IntVec bloqueoDetras = new IntVec(satWrapper.pool);
							// añadimos los literales a la clausula de bloqueo delante
							bloqueoDetras.add(listaLiterales.get(i+1).getLiteral());
							satWrapper.addModelClause(bloqueoDetras.toArray());// añadimos la clausula al satWrapper
						}
					}

				}

			}

			/*
			 * 
			 * REGLAS
			 * 
			 */

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
