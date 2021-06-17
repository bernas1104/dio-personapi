package one.digitalinnovation.personapi.service;

import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import one.digitalinnovation.personapi.utils.PersonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    @Test
    void testGivenPersonDTOThenReturnSavedMessage() {
        PersonDTO personDTO = PersonUtils.createFakeDTO();
        Person expectedSavedPerson = PersonUtils.createFakeEntity();

        Mockito.when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);
        Mockito.when(personMapper.toModel(personDTO)).thenReturn(expectedSavedPerson);

        MessageResponseDTO sucessMessage = personService.createPerson(personDTO);

        Assertions.assertEquals(
                "Created person with ID " + expectedSavedPerson.getId(),
                sucessMessage.getMessage()
        );
    }

    @Test
    void testGivenPeopleOnDBReturnListWithAllPeople() {
        Person person = PersonUtils.createFakeEntity();
        PersonDTO personDTO = PersonUtils.createFakeDTO();
        personDTO.setId(person.getId());

        List<Person> people = new ArrayList<>();
        people.add(person);

        Mockito.when(personRepository.findAll()).thenReturn(people);
        Mockito.when(personMapper.toDTO(person)).thenReturn(personDTO);

        List<PersonDTO> peopleDTO = personService.getAll();

        Assertions.assertEquals(1, peopleDTO.size());
        Assertions.assertEquals(peopleDTO.get(0).getId(), person.getId());
    }

    @Test
    void testGivenIdReturnPersonInformation() throws PersonNotFoundException {
        Person person = PersonUtils.createFakeEntity();
        PersonDTO expectedPersonDTO = PersonUtils.createFakeDTO();
        expectedPersonDTO.setId(person.getId());

        Mockito.when(personRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(person));
        Mockito.when(personMapper.toDTO(person)).thenReturn(expectedPersonDTO);

        PersonDTO personDTO = personService.getById(1L);

        Assertions.assertEquals(expectedPersonDTO, personDTO);
    }

    @Test
    void testGivenIdNoPersonFoundException() {
        Mockito.when(personRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.getById(1L));
    }

    @Test
    void testGivenIdUpdatesPersonInformation() {
        Person person = PersonUtils.createFakeEntity();

        PersonDTO personDTO = PersonUtils.createFakeDTO();
        personDTO.setId(person.getId());

        Mockito.when(personRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(person));
        Mockito.when(personRepository.save(any(Person.class)))
                .thenReturn(person);
        Mockito.when(personMapper.toModel(personDTO)).thenReturn(person);

        assertDoesNotThrow(() -> personService.updateById(1L, personDTO));
    }

    @Test
    void testGivenIdUpdateThrowsPersonNotFound() {
        PersonDTO personDTO = PersonUtils.createFakeDTO();

        Mockito.when(personRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.updateById(1L, personDTO));
    }

    @Test
    void testGivenIdDeletesPerson() throws PersonNotFoundException {
        Person person = PersonUtils.createFakeEntity();

        Mockito.when(personRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(person));

        personService.deleteById(1L);

        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGivenIdDeleteThrowsPersonNotFound() {
        Mockito.when(personRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.deleteById(1L));
    }
}
