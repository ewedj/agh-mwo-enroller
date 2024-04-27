package com.company.enroller.controllers;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

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
        return new ResponseEntity<Meeting>(meeting, HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        Meeting meeting1 = meetingService.findByID(meeting.getId());
        if (meeting1 != null) {
            return new ResponseEntity<>("Unable to create. A meeting with id " + meeting1.getId() + "already exist.", HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable long id, @RequestBody Meeting meeting) {
        Meeting meeting1 = meetingService.findByID(id);
        if (meeting != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        meetingService.remove(meeting1);
        return new ResponseEntity<>(HttpStatus.GONE);
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
}
