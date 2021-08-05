package ma.eurafric.eurbatchswift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import ma.eurafric.eurbatchswift.Repository.SwiftRepository;
import ma.eurafric.eurbatchswift.Repository.TypeChampRepository;
import ma.eurafric.eurbatchswift.Repository.TypeSwiftRepository;
import ma.eurafric.eurbatchswift.entities.Swift;
import ma.eurafric.eurbatchswift.entities.SwiftReader;
import ma.eurafric.eurbatchswift.entities.SwiftDetails;
import ma.eurafric.eurbatchswift.entities.TypeChamp;
import ma.eurafric.eurbatchswift.entities.TypeSwift;

public class SwiftItemProcess implements ItemProcessor<SwiftReader, Swift> {

	String msg = "";
	String sens = "";
	String block4 = "";
	String msgSwift = "";

	HashMap<String, String> listeFields = new HashMap<String, String>();

	@Autowired
	TypeSwiftRepository typeSwiftRepo;

	@Autowired
	SwiftRepository swiftRepository;

	@Autowired
	TypeChampRepository typeChampRepository;

	public Swift process(SwiftReader item) throws Exception {

		Swift swift = new Swift();
		List<SwiftDetails> swiftDetails = new ArrayList<SwiftDetails>();

		if (item.getField2().equals("")) {
			if (item.getField1().startsWith(":")) {
				msg += ";";
			}
			
			msgSwift += item.getField1();
			msg += item.getField1();
			
			return null;

		} else {

			msg += item.getField1();
			msgSwift += item.getField1();

			swift.setMessageSwift(msgSwift);

			if (msg.contains("{2:I")) {
				swift.setSens("Entrant");
			} else if (msg.contains("{2:O")) {
				swift.setSens("Sortant");
			}

			swift.setDateInsertion(new Date());

			swift.setTypeSwift(getTypeSwiftFromLibelle(msg, typeSwiftRepo));
			listeFields = getFields(msg);
			for (Entry<String, String> field : listeFields.entrySet()) {

				SwiftDetails detail = new SwiftDetails();

				detail.setTypeChamp(getDetailTypeChamp(field.getKey(), typeChampRepository, swift.getTypeSwift()));

				detail.setValue(field.getValue());
				detail.setSwift(swift);

				swiftDetails.add(detail);
			}
			swift.setDetails(swiftDetails);

			msg = item.getField2();
			msgSwift=msg;
			return swift;
		}
	}

	/**
	 * Cette methode retourne le type de swift d'un message swift
	 * 
	 * @param messageSwift
	 * @param tsr
	 * @return
	 */
	public TypeSwift getTypeSwiftFromLibelle(String messageSwift, TypeSwiftRepository tsr) {

		String s = org.apache.commons.lang3.StringUtils.substringBetween(messageSwift, "}{2:", "}{");
		List<TypeSwift> listTypeSwifts = tsr.findAll();

		for (TypeSwift t : listTypeSwifts) {
			if (t.getLibelle().equals("MT" + s.subSequence(1, 4)) == true) {
				return t;
			}
		}
		return null;
	}

	/**
	 * cette methode recupere le block 4 d'un message swift
	 * 
	 * @param message
	 * @return
	 */
	public String getBlock4(String message) {
		if (StringUtils.contains(message, "}}{4:;")) {

			return block4 = StringUtils.substringBetween(message, "}}{4:;", "}{5");

		} else if (StringUtils.contains(message, "N}{4:;")) {

			return block4 = StringUtils.substringBetween(message, "N}{4:;", "}{5");
		} else {
			return block4 = "";
		}
	}

	/**
	 * Cette methode retourne les champs d'un message swift sous forme d'une HashMap
	 * 
	 * @param msg
	 * @return
	 */
	public HashMap<String, String> getFields(String msg) {

		HashMap<String, String> fields = new HashMap<String, String>();

		String block4 = getBlock4(msg);
		String[] str = block4.split(";");

		List<String> al = new ArrayList<String>();
		al = Arrays.asList(str);

		for (String f : al) {

			fields.put(StringUtils.substringBetween(f, ":", ":"), StringUtils.substringAfterLast(f, ":"));

		}
		return fields;

	}

	/**
	 * Cette methode retourne le type de champ correspondant à chaque champ d'un
	 * message swift en fonction de son libelle et du type de swift
	 * 
	 * @param key
	 * @param typeChampRepository
	 * @param typeSwift
	 * @return
	 */
	public TypeChamp getDetailTypeChamp(String key, TypeChampRepository typeChampRepository, TypeSwift typeSwift) {

		List<TypeChamp> champs = typeChampRepository.findAll();

		for (TypeChamp champ : champs) {
			String libelleChamp = champ.getLibelle();
			if (key.equals(libelleChamp)) {
				champ.setTypeSwift(typeSwift);
				return champ;
			}
		}
		return null;

	}

}
