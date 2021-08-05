package ma.eurafric.eurbatchswift.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.eurafric.eurbatchswift.entities.SwiftDetails;;

public interface SwiftReaderRepository extends JpaRepository<SwiftDetails, Integer> {

}
