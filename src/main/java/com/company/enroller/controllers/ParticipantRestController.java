package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@RequestParam(name = "sortBy", required = false) String sortBy,
											 @RequestParam(name = "sortOrder", required = false) String sortOrder,
											 @RequestParam(name = "key", required = false) String key) {
		Collection<Participant> participants = participantService.getFilteredParticipants(key, sortBy, sortOrder);

		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant != null) {
			return new ResponseEntity<>("Unable to create. A participant with login " + participant.getLogin() + "already exist.", HttpStatus.CONFLICT);
		}
		participantService.add(participant);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
		Participant participant1 = participantService.findByLogin(login);
		if (participant != null) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		participantService.remove(participant1);
		return new ResponseEntity<>(HttpStatus.GONE);
	}
//----------------------------
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateParticipant(@RequestBody Participant participant, @PathVariable String id) {
		Participant participant1 = participantService.findByLogin(id);
		if (participant1 == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		participant1.setPassword(participant.getPassword());
		participantService.update(participant1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
