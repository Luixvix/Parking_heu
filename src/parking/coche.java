package parking;

public class coche {
	private int categoria;
	private int llegada;
	
	private int calle;
	private int n_plaza;
	
	public coche(int categoria, int llegada, int calle, int n_plaza) {
		this.categoria = categoria;
		this.llegada = llegada;
		this.calle = calle;
		this.n_plaza = n_plaza;
	}
	public int getCategoria() {
		return categoria;
	}
	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}
	public int getLlegada() {
		return llegada;
	}
	public void setLlegada(int llegada) {
		this.llegada = llegada;
	}
	public int getCalle() {
		return calle;
	}
	public void setCalle(int calle) {
		this.calle = calle;
	}
	public int getN_plaza() {
		return n_plaza;
	}
	public void setN_plaza(int n_plaza) {
		this.n_plaza = n_plaza;
	}
}
