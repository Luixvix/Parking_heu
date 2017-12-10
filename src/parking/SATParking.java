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
				for (int j = 0; j < plaza_x; j++) {
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
							categoria = 3;
							break;
						}
						coche c = new coche(categoria, llegada, i, j);
						BooleanVar cb_detras = new BooleanVar(store, "Coche, Cat: " + categoria + " Orden: " + llegada
								+ " Calle: " + i + " Plaza: " + j + " detras");
						BooleanVar cb_delante = new BooleanVar(store, "Coche, Cat: " + categoria + " Orden: " + llegada
								+ " Calle: " + i + " Plaza: " + j + " delante");
						listaVehiculos.add(cb_detras);
						listaVehiculos.add(cb_delante);
						satWrapper.register(cb_detras);
						satWrapper.register(cb_delante);
						c.setLiteralBDetras(satWrapper.cpVarToBoolVar(cb_detras, 1, true));
						c.setLiteralBDelante(satWrapper.cpVarToBoolVar(cb_delante, 1, true));
						coches.add(c);
						break;
					}
				}
			}
			BooleanVar array_vehiculos[] = new BooleanVar[listaVehiculos.size()];
			for (int i = 0; i < listaVehiculos.size(); i++) {
				array_vehiculos[i] = listaVehiculos.get(i);
			}

			/*
			 * 
			 * REGLAS
			 * 
			 */
			// Regla 1: Cada coche no debe estar bloqueado por delante y por
			// detras a la vez
			// Un valor TRUE indica que esta bloqueado en ese sentido, de forma
			// que si los dos son TRUE el resultado debe ser falso
			for (int i = 0; i < coches.size(); i++) {
				// (-Detras V -Delante)
				addClause(satWrapper, -coches.get(i).getLiteralBDetras(), -coches.get(i).getLiteralBDelante());
			}

			// Regla 2: Estados de bloqueo de cada coche por detras y por
			// delante.
			// En caso de estar bloqueado, se introduce el literal de forma
			// positiva, en caso de no estarlo se introduce de forma negativa


			// Estado de detras de cada coche
			for (int i = 0; i < coches.size(); i++) {
				int calle = coches.get(i).getCalle();
				int plaza = coches.get(i).getN_plaza();
				int categoria = coches.get(i).getCategoria();
				int llegada = coches.get(i).getLlegada();

				if (i - 1 < 0) {
					// Detras libre, dado que aunque el coche no esta al
					// comienzo de la calle, sabemos que estan en orden y por
					// tanto al ser el primero no hay otro coche detras
					IntVec estado = new IntVec(satWrapper.pool);
					estado.add(coches.get(i).getLiteralBDetras());
					satWrapper.addModelClause(estado.toArray());
					continue;
				}

				if (plaza == 0) {
					// detras libre, comienzo de la calle
					IntVec estado = new IntVec(satWrapper.pool);
					estado.add(coches.get(i).getLiteralBDetras());
					satWrapper.addModelClause(estado.toArray());
					continue;
				}
				// Coche situado detras
				if (coches.get(i - 1).getCalle() == calle && coches.get(i - 1).getN_plaza() == plaza - 1) {
					if (coches.get(i - 1).getCategoria() >= categoria) {
						if (coches.get(i - 1).getCategoria() > categoria) {
							// bloqueado porque es de categoria superior
							IntVec estado = new IntVec(satWrapper.pool);
							estado.add(-coches.get(i).getLiteralBDetras());
							satWrapper.addModelClause(estado.toArray());
						} else {
							// Es de la misma categoria
							if (coches.get(i - 1).getLlegada() > llegada) {
								// Bloqueado, misma categoria y llego despues
								IntVec estado = new IntVec(satWrapper.pool);
								estado.add(-coches.get(i).getLiteralBDetras());
								satWrapper.addModelClause(estado.toArray());
							} else {
								// libre porque llego antes y por tanto saldra
								// antes
								IntVec estado = new IntVec(satWrapper.pool);
								estado.add(coches.get(i).getLiteralBDetras());
								satWrapper.addModelClause(estado.toArray());
							}
						}
					} else {
						// libre porque es de categoria menor
						IntVec estado = new IntVec(satWrapper.pool);
						estado.add(coches.get(i).getLiteralBDetras());
						satWrapper.addModelClause(estado.toArray());
					}
				}
			}

			// Estado de delante de cada coche
			for (int i = 0; i < coches.size(); i++) {
				int calle = coches.get(i).getCalle();
				int plaza = coches.get(i).getN_plaza();
				int categoria = coches.get(i).getCategoria();
				int llegada = coches.get(i).getLlegada();

				if (i + 1 == coches.size()) {
					// delante libre, dado que aunque el coche no esta al
					// final de la calle, sabemos que estan en orden y por
					// tanto al ser el ultimo no hay otro coche delante
					IntVec estado = new IntVec(satWrapper.pool);
					estado.add(coches.get(i).getLiteralBDelante());
					satWrapper.addModelClause(estado.toArray());
					continue;
				}

				if (plaza == plaza_x - 1) {
					// delante libre, final de la calle
					IntVec estado = new IntVec(satWrapper.pool);
					estado.add(coches.get(i).getLiteralBDelante());
					satWrapper.addModelClause(estado.toArray());
					continue;
				}
				
				// Coche situado delante
				if (coches.get(i + 1).getCalle() == calle && coches.get(i + 1).getN_plaza() == plaza + 1) {
					if (coches.get(i + 1).getCategoria() >= categoria) {
						if (coches.get(i + 1).getCategoria() > categoria) {
							// bloqueado porque es de categoria superior
							IntVec estado = new IntVec(satWrapper.pool);
							estado.add(-coches.get(i).getLiteralBDelante());
							satWrapper.addModelClause(estado.toArray());
						} else {
							// Es de la misma categoria
							if (coches.get(i + 1).getLlegada() > llegada) {
								// Bloqueado, misma categoria y llego despues
								IntVec estado = new IntVec(satWrapper.pool);
								estado.add(-coches.get(i).getLiteralBDelante());
								satWrapper.addModelClause(estado.toArray());
							} else {
								// libre porque llego antes y por tanto saldra
								// antes
								IntVec estado = new IntVec(satWrapper.pool);
								estado.add(coches.get(i).getLiteralBDelante());
								satWrapper.addModelClause(estado.toArray());
							}
						}
					} else {
						// libre porque es de categoria menor
						IntVec estado = new IntVec(satWrapper.pool);
						estado.add(coches.get(i).getLiteralBDelante());
						satWrapper.addModelClause(estado.toArray());
					}
				}
			}

			// Resolucion del problema
			Search<BooleanVar> search = new DepthFirstSearch<BooleanVar>();
			SelectChoicePoint<BooleanVar> select = new SimpleSelect<BooleanVar>(array_vehiculos,
					new SmallestDomain<BooleanVar>(), new IndomainMin<BooleanVar>());
			result = search.labeling(store, select);

			r.closeFile(outputFile, map);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void addClause(SatWrapper satWrapper, int literal1, int literal2) {
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		satWrapper.addModelClause(clause.toArray());
	}

}
