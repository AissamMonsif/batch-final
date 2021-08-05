package ma.eurafric.eurbatchswift.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "type_champ")
public class TypeChamp {
	@Id
	@Column(name = "id_type_champ")
	private int idTypeChamp;

	@Column(name = "libelle")
	private String libelle;

	@Column(name = "description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "id_type_swift")
	private TypeSwift typeSwift;

	public TypeChamp() {
		super();
	}

	public int getIdTypeChamp() {
		return idTypeChamp;
	}

	public void setIdTypeChamp(int idTypeChamp) {
		this.idTypeChamp = idTypeChamp;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TypeSwift getTypeSwift() {
		return typeSwift;
	}

	public void setTypeSwift(TypeSwift typeSwift) {
		this.typeSwift = typeSwift;
	}

	@Override
	public String toString() {
		return "TypeChamp [idTypeChamp=" + idTypeChamp + ", libelle=" + libelle + ", description=" + description
				+ ", typeSwift=" + typeSwift + "]";
	}

}
