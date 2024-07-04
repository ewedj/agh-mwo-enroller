package com.company.enroller.controllers;

import java.util.Collection;
import java.util.List;

import com.company.enroller.model.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting() {

        Collection<Meeting> meetings = meetingService.getAll();

        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
        Meeting meeting = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        meetingService.add(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable long id, @RequestBody Meeting meeting) {
        Meeting meeting1 = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meetingService.remove(meeting1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable long id, @RequestBody Meeting meeting) {
        Meeting meeting1 = meetingService.findByID(id);
        if (meeting1 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meeting1.setTitle(meeting.getTitle());
        meeting1.setDate(meeting.getDate());
        meeting1.setDescription(meeting.getDescription());
        meetingService.update(meeting1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable long id) {
        Meeting meeting1 = meetingService.findByID(id);
        if (meeting1 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meeting1.getParticipants(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeetingParticipant(@PathVariable long id, @PathVariable String login) {
        Meeting meeting = meetingService.findByID(id);
        Participant participant = participantService.findByLogin(login);

        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.GONE);
        }

        if (!meeting.getParticipants().contains(participant)) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        meeting.getParticipants().remove(participant);
        meetingService.update(meeting);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable long id, @RequestBody List<String> participantsLogins) {
        Meeting meeting = meetingService.findByID(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        for (String login : participantsLogins) {
            Participant participant = participantService.findByLogin(login);
            if (participant == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            meeting.addParticipant(participant);
        }
        meetingService.update(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
