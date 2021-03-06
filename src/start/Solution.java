package start;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

	static class PairValues { // Clasa folosita pentru a retine coordonatele unui punct
		private Float x;
		private Float y;

		PairValues(Float x, Float y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() { 
			return this.x + " " + this.y;
		}

		@Override
		public boolean equals(Object o) { // Metoda suprascrisa pentru a compara 2 instante a clasei PairValues. 
										  //  Necesara pentru functia .contains() din HashSet
			if (o == this) {
				return true;
			}

			if (!(o instanceof PairValues)) {
				return false;
			}

			PairValues c = (PairValues) o;

			return ((Float.compare(c.x,this.x)==0) && (Float.compare(c.y,this.y)==0));
		}
		
		@Override
	    public int hashCode() {  // Generarea unui hashcode care sa fie unic pentru fiecare instanta
								 // Necesara pentru functia .conatains din HashSet
			
			int result = 17; // Am ales numerele prime 17 si 31 pentru a obtine un hashcode unic.
	        result = 31 * result + Math.round(x); 
	        result = 31 * result + Math.round(y);
	        return result;
	    }


		public void setX(Float x) {
			this.x = x;
		}

		public void setY(Float y) {
			this.y = y;
		}

		public Float getX() {
			return this.x;
		}

		public Float getY() {
			return this.y;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		
		// Folosesc clasa Scanner pentru a citi din fisierul "input.txt" punctele
		File f = new File("input.txt");
		Scanner s = new Scanner(f);
		
		// In HashMap o sa retin astfel datele:
		// Key -> coordonata x
		// Value -> o lista ce contine coordonata y de la fiecare punct ce are coordonata x egala cu cheia
		Map<Float, List<Float>> map = new <Float, List<Float>>HashMap();
		
		// In HashSet o sa retinem toate punctele
		Set<PairValues> set = new HashSet<PairValues>();

		while (s.hasNext()) {
			String newLine = s.nextLine();
			// Folosesc Regex pentru a extrage fiecare punct in parte
			// Merge pentru numere negative si cu parte zecimala
			Pattern pattern = Pattern.compile("(\\(\\-*[0-9]\\.*[0-9]*\\,\\-*[0-9]\\.*[0-9]*\\))");

			Matcher m = pattern.matcher(newLine);

			while (m.find()) {
				String group = m.group();
				pattern = Pattern.compile("(-*[0-9]\\.*[0-9]*)");
				Matcher m2 = pattern.matcher(group);
				
				// Convertim String-urile obtinute in Float
				m2.find();
				// Coordonata x
				float x = Float.parseFloat(m2.group());
				m2.find();
				// Coordonata y
				float y = Float.parseFloat(m2.group());
				
				// Daca nu am inserat deja in HashMap la cheia x, cream un nou ArrayList
				if (map.get(x) == null) {
					List<Float> newList = new ArrayList<Float>();
					newList.add(y);
					map.put(x, newList);
				} else {
					List<Float> newList = map.get(x);
					// Adaugam la cheia x, coordoanata y
					newList.add(y);
					map.put(x, newList);
				}
				
				// Adaugam in set perechea (x,y)
				set.add(new PairValues(x,y));
				
			}
		}
		s.close();
		
		// Extragem valorile din HashMap si le adaugam intr-un ArrayList pentru a parcurge mai usor valorile
		Set<Map.Entry<Float, List<Float>>> entrySet = map.entrySet();
		ArrayList<Map.Entry<Float, List<Float>>> arr = new ArrayList<Map.Entry<Float, List<Float>>>(entrySet);
		
		// In aceasta variabila o sa stocam numarul dreptunghiurilor gasite
		int total = 0;
		
		// Ideea algoritmului este de a parcurge punctele, 2 cate 2, si sa interpretam dreapta ca diagonala unui posibil dreptunghi
		// Dupa ce am obtinut diagonala din punctele (x1,y1) si (x2,y1), 
		// trebuie sa mai verificam daca exista varfurile (x1,y2) si (x2,y1).
		// Daca exista, putem spune ca am gasit un nou dreptunghi.
		
		// Aici putem observa de ce am ales sa folosesc structura HashMap.
		// Stim prea bine ca daca diagoanala este pe aceeasi axa, nu putem obtine un dreptunghi.
		// Folosind structura HashMap, o sa parcurg punctele astfel incat nu o sa compar 2 puncte care au aceeasi coordonata x
		
		// Primul si al doilea for vor itera de la o serie la alta (seria are aceeasi coordonata x)
		// Al treilea si al patrulea for vor itera prin elementele fiecarei serii.
		
		// Aceasta metoda este costisitoare din punctul de vedere al memoriei, dar la un numar mai mare de date, va da dovada de eficienta
		
		for (int i = 0; i < arr.size(); i++) { // Coordonata x - primul punct
			for (int j = i + 1; j < arr.size(); j++) { // Coordonata x - al doilea punct
				for (Float z : arr.get(i).getValue()) { // Coordonata y - primul punct 
					for (Float x : arr.get(j).getValue()) { // Cordonata y - al doilea punct
						if (Float.compare(x,z)!=0) { // Daca punctele se afla pe aceeasi axa OY,nu putem obtine un dreptunghi
							
							// Punctele care trebuie verificate
							PairValues p1 = new PairValues(arr.get(i).getKey(), x);
							PairValues p2 = new PairValues(arr.get(j).getKey(), z);
							// In aceasta conditie verificam daca exista aceste puncte
							if (set.contains(p1) && set.contains(p2)) {
								total++;
							}
						}
					}
				}
			}
		}
		
		// Impartim la 2 deoarece in parcurgere am verificat 2 diagonale ce apartine aceluiasi dreptunghi
		System.out.println("Total este "+total/2);
	}

}
