package ma.eurafric.eurbatchswift.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "swift_details")
public class SwiftDetails {
	@Id
	@GeneratedValue	(strategy = GenerationType.AUTO)
	@Column(name = "id_swift_details")
	private int idSwiftDetails;

	@ManyToOne
	@JoinColumn(name = "id_swift")
	private Swift swift;

	@Column(name = "value")
	@Lob//, columnDefinition = "text")
	private String value;

	@ManyToOne
	@JoinColumn(name = "id_type_champ")
	private TypeChamp typeChamp;

	public int getIdSwiftDetails() {
		return idSwiftDetails;
	}

	public void setIdSwiftDetails(int idSwiftDetails) {
		this.idSwiftDetails = idSwiftDetails;
	}

	public Swift getSwift() {
		return swift;
	}

	public void setSwift(Swift swift) {
		this.swift = swift;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TypeChamp getTypeChamp() {
		return typeChamp;
	}

	public void setTypeChamp(TypeChamp typeChamp) {
		this.typeChamp = typeChamp;
	}

	@Override
	public String toString() {
		return "SwiftDetails [idSwiftDetails=" + idSwiftDetails + ", swift=" + swift + ", value=" + value
				+ ", typeChamp=" + typeChamp + "]";
	}

	public SwiftDetails() {
		super();
	}
}
