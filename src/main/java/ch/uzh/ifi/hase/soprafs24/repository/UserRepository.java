package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
  User findByName(String name);

  User findByUsername(String username);

  User findByToken(String token);
  
  boolean existsByUsername(String username);
}

/*
 * Some methods provided by JpaRepository and can be used to manipulate date in datebase
 * 
 * save(EntityClass entity): Saves the given entity. If the entity has not been persisted before, 
 * it will be saved as a new entity. If the entity has been persisted before, it will be updated.
 * 
 * saveAndFlush(EntityClass entity): save and entityManager.flush(sent sql need to be conducted to the database)
 * 
 * Delete:
   deleteById(ID id): Deletes the entity with the given ID.
   delete(T entity): Deletes the given entity.
   deleteAll(): Deletes all entities managed by the repository.
   
   Find:
   findById(ID id): Retrieves an entity by its ID.
   findAll(): Retrieves all entities managed by the repository.
   findAllById(Iterable<ID> ids): Retrieves entities by their IDs.
   count(): Returns the number of entities managed by the repository.

   Existence check:
   existsById(ID id): Checks if an entity with the given ID exists.

   Custom queries:
   findByName(String name)
   findByXXXAndYYY use the "And" keyword, similarly there are "Or"
   findByZZZisNull
 */