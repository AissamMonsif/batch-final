package ma.eurafric.eurbatchswift.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "type_swift")
public class TypeSwift {
	@Id
	@Column(name = "id_type_swift")
	private int idTypeSwift;

	@Column(name = "libelle")
	private String libelle;

	@Column(name = "designation")
	private String designation;

	@Column(name = "categorie")
	private String categorie;

	public TypeSwift() {
		super();
	}

	public int getIdTypeSwift() {
		return idTypeSwift;
	}

	public void setIdTypeSwift(int idTypeSwift) {
		this.idTypeSwift = idTypeSwift;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	@Override
	public String toString() {
		return "TypeSwift [idTypeSwift=" + idTypeSwift + ", libelle=" + libelle + ", designation=" + designation
				+ ", categorie=" + categorie + "]";
	}

}
