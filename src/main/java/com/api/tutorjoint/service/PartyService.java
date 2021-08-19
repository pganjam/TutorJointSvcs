package com.api.tutorjoint.service;

import java.util.List;

import com.api.tutorjoint.exception.PartyNotFoundException;
import com.api.tutorjoint.model.Party;
import com.api.tutorjoint.repository.PartyRepository;
import com.api.tutorjoint.security.services.UserDetailsImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartyService {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private AuthService authService;

    public Party getPartyById(Long id) throws Exception {
        try {
            Party party = partyRepository.findById(id).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));
            return party;
        } catch (PartyNotFoundException partyNotFound) {
            throw partyNotFound;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("There is not Party for party record - " + id);
        }
    }

    public Party getPartyForCurrentUser() throws Exception {
        try {
            UserDetailsImpl loggedInUser = authService.getCurrentUser().orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

            Party party = partyRepository.findById(loggedInUser.getId()).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));

            return party;
        } catch (UsernameNotFoundException userNotFound) {
            throw userNotFound;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to get Party Record for Current User");
        }
    }

    public Party getPartyByEmailId(String email) throws Exception {
        try {
            Party party = partyRepository.findByEmailAddress(email).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));
            return party;
        } catch (PartyNotFoundException partyNotFound) {
            throw partyNotFound;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to get Party with emailId- " + email);
        }
    }

    public List<Party> getAllParties(String partyType) throws Exception {
        try {
            List<Party> partyList = partyRepository.findByPartyType(partyType);
            if (partyList == null)
                throw new UsernameNotFoundException("No Parties Found");
            else
                return partyList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error:  Unable to get Parties");
        }
    }

    @Transactional
    public Long addParty(Party party) throws Exception {
        try {
            Party partyObj = partyRepository.findByEmailAddress(party.getEmailAddress()).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));
            if (partyObj != null) {
                throw new DuplicateKeyException("Party exists already!!");
            }
        } catch (PartyNotFoundException partyNotFound) {
        } catch (DuplicateKeyException dk) {
            throw dk;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to get Party with emailId- " + party.getEmailAddress());
        }

        String partyId = RandomStringUtils.randomAlphanumeric(20);
        try {
            Party p = partyRepository.save(party);
            return p.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to Save Party with emailId- " + party.getEmailAddress());
        }
    }

    @Transactional
    public String updateParty(Party party) throws Exception {
        try {
            Party partyObj = partyRepository.findByEmailAddress(party.getEmailAddress()).orElseThrow(() -> new PartyNotFoundException("Party Not Found"));
            party.setPartyType(party.getPartyType());
            party.setId(partyObj.getId());
            partyRepository.save(party);

            return Long.toString(partyObj.getId());
        } catch (PartyNotFoundException partyNotFound) {
            throw partyNotFound;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to find Party with emailId- " + party.getEmailAddress());
        }
    }

    @Transactional
    public boolean deleteParty(Long id) throws Exception {
        try {

            partyRepository.delete(getPartyById(id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Data Access Error: Unable to Remove Party with Id- " + id);
        }
    }
}
