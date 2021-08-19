package com.api.tutorjoint.service;

import com.api.tutorjoint.dto.TutorSlotDto;
import com.api.tutorjoint.exception.DataNotFoundException;
import com.api.tutorjoint.exception.PartyNotFoundException;
import com.api.tutorjoint.model.Appointment;
import com.api.tutorjoint.model.Slot;
import com.api.tutorjoint.model.Tutor;
import com.api.tutorjoint.model.TutorSlot;
import com.api.tutorjoint.repository.AppointmentRepository;
import com.api.tutorjoint.repository.SlotRepository;
import com.api.tutorjoint.repository.TutorRepository;
import com.api.tutorjoint.repository.TutorSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private TutorSlotRepository tutorSlotRepository;

    @Transactional
    public boolean addAppointments(Long tutor_id, List<TutorSlotDto> listAppointmentDto) throws PartyNotFoundException {
        Tutor tutor = tutorRepository.findById(tutor_id).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found"));

        Instant date = listAppointmentDto.get(0).getCreatedDate();
        List<Appointment> listAppointments = appointmentRepository.findByTutorAndDate(tutor, date);
        List<String> strList = new ArrayList<>();
        for (Appointment element : listAppointments) {
            strList.add(element.getTimeSlot());
        }

        for (TutorSlotDto dto : listAppointmentDto) {
            if (strList.contains(dto.getValue()))
                throw new RuntimeException("Appointment not available");
        }

        for (TutorSlotDto dto : listAppointmentDto) {
            Appointment a = new Appointment();
            a.setTutor(tutor);
            a.setDate(dto.getCreatedDate());
            a.setTimeSlot(dto.getValue());
            a.setCreatedOn(Instant.now());
            a.setUpdatedOn(Instant.now());
            appointmentRepository.save(a);
        }
        return true;
    }

    public List<TutorSlotDto> getAvailability(Long tutorId, Instant date) throws PartyNotFoundException {
        Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found"));
        ArrayList<TutorSlotDto> appointments = new ArrayList<TutorSlotDto>();
        List<Appointment> appointmentList = appointmentRepository.findByTutorAndDate(tutor, date);
        for (Appointment element : appointmentList) {
            TutorSlotDto slot = new TutorSlotDto();
            slot.setTutor_id(tutorId.toString());
            slot.setValue(element.getTimeSlot());
            slot.setCreatedDate(date);
            slot.setStatus("BUSY");
            appointments.add(slot);
        }
        return appointments;
    }

    public List<TutorSlotDto> getOfficeHours(Long tutorId, String measure) throws PartyNotFoundException {
        Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found"));

        ArrayList<TutorSlotDto> officeHours = new ArrayList<TutorSlotDto>();
        List<TutorSlot> tutorSlotList = tutorSlotRepository.findByTutor(tutor);

        if (tutorSlotList.isEmpty()) {
            List<Slot> slots = slotRepository.findByMeasure(measure);

            for (Slot element :
                    slots) {
                TutorSlot tutorSlot = new TutorSlot();
                tutorSlot.setId(element.getId());
                tutorSlot.setCreatedOn(Instant.now());
                tutorSlot.setUpdatedOn((Instant.now()));
                tutorSlot.setStatus("CLOSED");
                tutorSlot.setMeasure(element.getMeasure());
                tutorSlot.setTutor(tutor);
                tutorSlot.setValue(element.getValue());

                tutorSlotRepository.save(tutorSlot);
                tutorSlotList.add(tutorSlot);
            }
        }

        for (TutorSlot element :
                tutorSlotList) {
            TutorSlotDto slot = new TutorSlotDto();
            slot.setId(element.getId());
            slot.setTutor_id(tutorId.toString());
            slot.setMeasure(element.getMeasure());
            slot.setValue(element.getValue());
            slot.setStatus(element.getStatus());
            slot.setCreatedDate(element.getCreatedOn());
            officeHours.add(slot);
        }

        return officeHours;
    }

    public void updateOfficeHours(Long tutorId, List<TutorSlotDto> tutorSlotDtoList) throws RuntimeException {
        Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found"));

        for (TutorSlotDto element :
                tutorSlotDtoList) {
            if (element.getStatus().equals("OPEN")) {
                TutorSlot slot = tutorSlotRepository.findById(element.getId()).orElseThrow(() -> new DataNotFoundException("Data Not Found"));
                slot.setStatus("OPEN");
                tutorSlotRepository.save(slot);
            } else {
                if (element.getId() != -1) {
                    TutorSlot slot = tutorSlotRepository.findById(element.getId()).orElseThrow(() -> new DataNotFoundException("Data Not Found"));
                    slot.setStatus("CLOSED");
                    tutorSlotRepository.save(slot);
                }
            }
        }
    }
}
