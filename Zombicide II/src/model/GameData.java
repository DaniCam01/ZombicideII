package model;

import java.io.Serializable;
import java.util.ArrayList;

import sprites.Botiquin;
import sprites.Disparo;
import sprites.Maquina;
import sprites.Mesa;
import sprites.Player;
import sprites.Zombie;
import sprites.ZombieBig;
import util.Assets;

public class GameData implements Serializable {

	@Override
	public String toString() {
		return "GameData [players=" + players + "]";
	}
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Player> players;
	private ArrayList<Disparo> disparos;
	private ArrayList<Mesa> mesas;
	private ArrayList<Maquina> maquinas;
	private ArrayList<Zombie> zombies;
	private ArrayList<ZombieBig> zombiesbig;
	private ArrayList<Botiquin> botiquines;
	private int vidas;
	private int rondas;
	private int pjvivos;
	private boolean sounddisparo;
	private boolean soundheal;
	private boolean soundgameover;
	private boolean sounddead;
	private boolean soundcash;
	private boolean soundevil;
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public ArrayList<Disparo> getDisparos() {
		return disparos;
	}
	public void setDisparos(ArrayList<Disparo> disparos) {
		this.disparos = disparos;
	}
	public ArrayList<Mesa> getMesas() {
		return mesas;
	}
	public void setMesas(ArrayList<Mesa> mesas) {
		this.mesas = mesas;
	}
	public ArrayList<Maquina> getMaquinas() {
		return maquinas;
	}
	public void setMaquinas(ArrayList<Maquina> maquinas) {
		this.maquinas = maquinas;
	}
	public ArrayList<Zombie> getZombies() {
		return zombies;
	}
	public void setZombies(ArrayList<Zombie> zombies) {
		this.zombies = zombies;
	}
	public int getVidas() {
		return vidas;
	}
	public void setVidas(int vidas) {
		this.vidas = vidas;
	}
	public int getRondas() {
		return rondas;
	}
	public void setRondas(int rondas) {
		this.rondas = rondas;
	}
	public int getPjvivos() {
		return pjvivos;
	}
	public void setPjvivos(int pjvivos) {
		this.pjvivos = pjvivos;
	}
	public boolean isSounddisparo() {
		return sounddisparo;
	}
	public void setSounddisparo(boolean sounddisparo) {
		this.sounddisparo = sounddisparo;
	}
	public boolean isSoundheal() {
		return soundheal;
	}
	public void setSoundheal(boolean soundheal) {
		this.soundheal = soundheal;
	}
	public boolean isSoundgameover() {
		return soundgameover;
	}
	public void setSoundgameover(boolean soundgameover) {
		this.soundgameover = soundgameover;
	}
	public boolean isSounddead() {
		return sounddead;
	}
	
	public void setSoundcash(boolean soundcash) {
		this.soundcash = soundcash;
	}
	public boolean isSoundcash() {
		return soundcash;
	}
	public void setSounddead(boolean sounddead) {
		this.sounddead = sounddead;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public GameData() {
		super();
	}
	public void setBoti(ArrayList<Botiquin> botis) {
		this.botiquines = botis;
	}
	public ArrayList<Botiquin> getBotis() {
		return botiquines;
	}
	public ArrayList<ZombieBig> getZombiesbig() {
		return zombiesbig;
	}
	public void setZombiesbig(ArrayList<ZombieBig> zombiesbig) {
		this.zombiesbig = zombiesbig;
	}
	public boolean isSoundevil() {
		return soundevil;
	}
	public void setSoundevil(boolean soundevil) {
		this.soundevil = soundevil;
	}
	
}
