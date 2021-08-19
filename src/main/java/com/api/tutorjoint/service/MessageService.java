package com.api.tutorjoint.service;

import com.api.tutorjoint.dto.DiscussionDto;
import com.api.tutorjoint.exception.CourseNotFoundException;
import com.api.tutorjoint.exception.DataNotFoundException;
import com.api.tutorjoint.exception.PartyNotFoundException;
import com.api.tutorjoint.model.*;
import com.api.tutorjoint.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private IMessageRepository iMessageRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyService partyService;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Transactional
    public long createDiscussion(DiscussionDto discussionDto) throws RuntimeException {

        Party party = null;
        try {
            party = partyRepository.findById(discussionDto.getParty_id()).orElseThrow(() -> new PartyNotFoundException("User Not Found"));
        } catch (Exception e) {
            throw e;
        }

        Course course = null;
        try {
            course = courseRepository.findById(discussionDto.getCourse_id()).orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
        } catch (Exception e) {
            throw e;
        }

        IMessage message = new IMessage();
        message.setTitle(discussionDto.getTitle());
        message.setMessage(discussionDto.getMessage());
        message.setCreatedOn(Instant.now());
        message.setUpdatedOn(Instant.now());
        message.setParty(party);

        Discussion discussion = new Discussion();
        discussion.setCourse(course);
        discussion.setMessage(message);
        discussion.setComments(null);

        discussionRepository.save(discussion);

        return discussion.getDiscussion_id();
    }

    @Transactional
    public long updateDiscussion(DiscussionDto discussionDto) throws RuntimeException {

        Party party = null;
        try {
            party = partyRepository.findById(discussionDto.getParty_id()).orElseThrow(() -> new PartyNotFoundException("User Not Found"));
        } catch (Exception e) {
            throw e;
        }

        Course course = null;
        try {
            course = courseRepository.findById(discussionDto.getCourse_id()).orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
        } catch (Exception e) {
            throw e;
        }

        Discussion discussion = discussionRepository.findById(discussionDto.getId()).orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));

        IMessage message = discussion.getMessage();
        message.setTitle(discussionDto.getTitle());
        message.setMessage(discussionDto.getMessage());
        message.setUpdatedOn(Instant.now());

        discussion.setMessage(message);
        discussionRepository.save(discussion);

        return discussionDto.getId();
    }

    @Transactional
    public void deleteDiscussion(long id) throws RuntimeException {
        Discussion discussion = discussionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));
        discussionRepository.delete(discussion);

        IMessage im = discussion.getMessage();
        iMessageRepository.delete(im);
    }

    public List<DiscussionDto> getTutorDiscussions(Long tutor_id) {
        Tutor tutor = tutorRepository.findById(tutor_id).orElseThrow(() -> new PartyNotFoundException("Tutor does not exist"));

        List<Course> courses = courseRepository.findByTutor(tutor, Sort.by(Sort.Direction.DESC, "createdOn"));
        List<DiscussionDto> discussionsDto = new ArrayList<>();

        for (Course course : courses) {

            List<Discussion> discussions = discussionRepository.findByCourse(course);
            for (Discussion discussion : discussions) {
                DiscussionDto discussionDto = new DiscussionDto();
                discussionDto.setId(discussion.getDiscussion_id());
                discussionDto.setTitle(discussion.getMessage().getTitle());
                discussionDto.setMessage(discussion.getMessage().getMessage());
                discussionDto.setParty_id(discussion.getMessage().getParty().getId());
                discussionDto.setUsername(discussion.getMessage().getParty().getUser().getUsername());
                discussionDto.setCourse_id(discussion.getCourse().getId());
                discussionDto.setCreatedOn(discussion.getMessage().getCreatedOn());
                discussionDto.setUpdatedOn(discussion.getMessage().getUpdatedOn());
                List<DiscussionDto> commentsDto = new ArrayList<>();
                for (Comment comment : discussion.getComments()) {
                    DiscussionDto commentDto = new DiscussionDto();
                    commentDto.setId(comment.getMessage().getId());
                    commentDto.setTitle(comment.getMessage().getTitle());
                    commentDto.setMessage(comment.getMessage().getMessage());
                    commentDto.setParty_id(comment.getMessage().getParty().getId());
                    commentDto.setUsername(comment.getMessage().getParty().getUser().getUsername());
                    commentDto.setCreatedOn(comment.getMessage().getCreatedOn());
                    commentDto.setUpdatedOn(comment.getMessage().getUpdatedOn());
                    commentsDto.add(commentDto);
                }
                discussionDto.setComments(commentsDto);
                discussionsDto.add(discussionDto);
            }
        }
        return discussionsDto;
    }

    public List<DiscussionDto> getStudentDiscussions(Long student_id) {
        Party student = partyRepository.findById(student_id).orElseThrow(() -> new PartyNotFoundException("Student does not exist"));

        List<Enrollment> enrollments = enrollmentRepository.findByParty(student, Sort.by(Sort.Direction.DESC, "createdOn"));
        List<DiscussionDto> discussionsDto = new ArrayList<>();

        for (Enrollment enrollment : enrollments
        ) {
            Course course = enrollment.getCourse();
            List<Discussion> discussions = discussionRepository.findByCourse(course);
            for (Discussion discussion : discussions) {
                DiscussionDto discussionDto = new DiscussionDto();
                discussionDto.setId(discussion.getDiscussion_id());
                discussionDto.setTitle(discussion.getMessage().getTitle());
                discussionDto.setMessage(discussion.getMessage().getMessage());
                discussionDto.setParty_id(discussion.getMessage().getParty().getId());
                discussionDto.setUsername(discussion.getMessage().getParty().getUser().getUsername());
                discussionDto.setCourse_id(discussion.getCourse().getId());
                discussionDto.setCreatedOn(discussion.getMessage().getCreatedOn());
                discussionDto.setUpdatedOn(discussion.getMessage().getUpdatedOn());
                List<DiscussionDto> commentsDto = new ArrayList<>();
                for (Comment comment : discussion.getComments()) {
                    DiscussionDto commentDto = new DiscussionDto();
                    commentDto.setId(comment.getMessage().getId());
                    commentDto.setTitle(comment.getMessage().getTitle());
                    commentDto.setMessage(comment.getMessage().getMessage());
                    commentDto.setParty_id(comment.getMessage().getParty().getId());
                    commentDto.setUsername(comment.getMessage().getParty().getUser().getUsername());
                    commentDto.setCreatedOn(comment.getMessage().getCreatedOn());
                    commentDto.setUpdatedOn(comment.getMessage().getUpdatedOn());
                    commentsDto.add(commentDto);
                }
                discussionDto.setComments(commentsDto);
                discussionsDto.add(discussionDto);
            }
        }
        return discussionsDto;
    }

    public List<DiscussionDto> getCourseDiscussions(Long course_id) {
        Course course = courseRepository.findById(course_id).orElseThrow(() -> new CourseNotFoundException("Course does not exist"));

        List<Discussion> discussions = discussionRepository.findByCourse(course);

        List<DiscussionDto> discussionsDto = new ArrayList<>();

        for (Discussion discussion : discussions) {
            DiscussionDto discussionDto = new DiscussionDto();
            discussionDto.setId(discussion.getDiscussion_id());
            discussionDto.setTitle(discussion.getMessage().getTitle());
            discussionDto.setMessage(discussion.getMessage().getMessage());
            discussionDto.setParty_id(discussion.getMessage().getParty().getId());
            discussionDto.setUsername(discussion.getMessage().getParty().getUser().getUsername());
            discussionDto.setCourse_id(discussion.getCourse().getId());
            discussionDto.setCreatedOn(discussion.getMessage().getCreatedOn());
            discussionDto.setUpdatedOn(discussion.getMessage().getUpdatedOn());
            List<DiscussionDto> commentsDto = new ArrayList<>();
            for (Comment comment : discussion.getComments()) {

                DiscussionDto commentDto = new DiscussionDto();
                commentDto.setId(comment.getMessage().getId());
                commentDto.setTitle(comment.getMessage().getTitle());
                commentDto.setMessage(comment.getMessage().getMessage());
                commentDto.setParty_id(comment.getMessage().getParty().getId());
                commentDto.setUsername(comment.getMessage().getParty().getUser().getUsername());
                commentDto.setCreatedOn(comment.getMessage().getCreatedOn());
                commentDto.setUpdatedOn(comment.getMessage().getUpdatedOn());
                commentsDto.add(commentDto);
            }
            discussionDto.setComments(commentsDto);
            discussionsDto.add(discussionDto);
        }
        return discussionsDto;
    }

    @Transactional
    public long createAnnouncement(DiscussionDto discussionDto) throws RuntimeException {

        Party party = null;
        try {
            party = partyRepository.findById(discussionDto.getParty_id()).orElseThrow(() -> new PartyNotFoundException("User Not Found"));
        } catch (Exception e) {
            throw e;
        }

        Course course = null;
        try {
            course = courseRepository.findById(discussionDto.getCourse_id()).orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
        } catch (Exception e) {
            throw e;
        }

        IMessage message = new IMessage();
        message.setTitle(discussionDto.getTitle());
        message.setMessage(discussionDto.getMessage());
        message.setCreatedOn(Instant.now());
        message.setUpdatedOn(Instant.now());
        message.setParty(party);

        Announcement announcement = new Announcement();
        announcement.setCourse(course);
        announcement.setMessage(message);

        announcementRepository.save(announcement);

        return announcement.getAnnouncement_id();
    }

    @Transactional
    public long updateAnnouncement(DiscussionDto discussionDto) throws RuntimeException {

        Party party = null;
        try {
            party = partyRepository.findById(discussionDto.getParty_id()).orElseThrow(() -> new PartyNotFoundException("User Not Found"));
        } catch (Exception e) {
            throw e;
        }

        Course course = null;
        try {
            course = courseRepository.findById(discussionDto.getCourse_id()).orElseThrow(() -> new CourseNotFoundException("Course Not Found"));
        } catch (Exception e) {
            throw e;
        }

        Announcement announcement = announcementRepository.findById(discussionDto.getId()).orElseThrow(() -> new DataNotFoundException("Announcement Not Found"));

        IMessage message = announcement.getMessage();
        message.setTitle(discussionDto.getTitle());
        message.setMessage(discussionDto.getMessage());
        message.setUpdatedOn(Instant.now());

        announcement.setMessage(message);
        announcementRepository.save(announcement);

        return discussionDto.getId();
    }

    @Transactional
    public void deleteAnnouncement(long id) throws RuntimeException {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));
        announcementRepository.delete(announcement);

        IMessage im = announcement.getMessage();
        iMessageRepository.delete(im);
    }

    public List<DiscussionDto> getTutorAnnouncements(Long tutor_id) {
        Tutor tutor = tutorRepository.findById(tutor_id).orElseThrow(() -> new PartyNotFoundException("Tutor does not exist"));

        List<Course> courses = courseRepository.findByTutor(tutor, Sort.by(Sort.Direction.DESC, "createdOn"));
        List<DiscussionDto> announcementsDto = new ArrayList<>();

        for (Course course : courses) {

            List<Announcement> announcements = announcementRepository.findByCourse(course);
            for (Announcement announcement : announcements) {

                DiscussionDto announcementDto = new DiscussionDto();
                announcementDto.setId(announcement.getAnnouncement_id());
                announcementDto.setTitle(announcement.getCourse().getTitle() + "-" + announcement.getMessage().getTitle());
                announcementDto.setMessage(announcement.getMessage().getMessage());
                announcementDto.setParty_id(announcement.getMessage().getParty().getId());
                announcementDto.setUsername(announcement.getMessage().getParty().getUser().getUsername());
                announcementDto.setCourse_id(announcement.getCourse().getId());
                announcementDto.setCreatedOn(announcement.getMessage().getCreatedOn());
                announcementDto.setUpdatedOn(announcement.getMessage().getUpdatedOn());
                announcementDto.setComments(null);

                announcementsDto.add(announcementDto);
            }

        }
        return announcementsDto;
    }

    public List<DiscussionDto> getCourseAnnouncements(Long course_id) {
        Course course = courseRepository.findById(course_id).orElseThrow(() -> new CourseNotFoundException("Course does not exist"));

        List<Announcement> announcements = announcementRepository.findByCourse(course);

        List<DiscussionDto> announcementsDto = new ArrayList<>();

        for (Announcement announcement : announcements) {
            DiscussionDto announcementDto = new DiscussionDto();
            announcementDto.setId(announcement.getAnnouncement_id());
            announcementDto.setTitle(announcement.getMessage().getTitle());
            announcementDto.setMessage(announcement.getMessage().getMessage());
            announcementDto.setParty_id(announcement.getMessage().getParty().getId());
            announcementDto.setUsername(announcement.getMessage().getParty().getUser().getUsername());
            announcementDto.setCourse_id(announcement.getCourse().getId());
            announcementDto.setCreatedOn(announcement.getMessage().getCreatedOn());
            announcementDto.setUpdatedOn(announcement.getMessage().getUpdatedOn());
            announcementDto.setComments(null);
            announcementsDto.add(announcementDto);
        }
        return announcementsDto;
    }

    public List<DiscussionDto> getStudentAnnouncements(Long student_id) {
        Party student = partyRepository.findById(student_id).orElseThrow(() -> new PartyNotFoundException("Student does not exist"));

        List<Enrollment> enrollments = enrollmentRepository.findByParty(student, Sort.by(Sort.Direction.DESC, "createdOn"));

        List<DiscussionDto> announcementsDto = new ArrayList<>();
        for (Enrollment enrollment : enrollments
        ) {
            Course course = enrollment.getCourse();
            List<Announcement> announcements = announcementRepository.findByCourse(course);

            for (Announcement announcement : announcements) {
                DiscussionDto announcementDto = new DiscussionDto();
                announcementDto.setId(announcement.getAnnouncement_id());
                announcementDto.setTitle(announcement.getMessage().getTitle());
                announcementDto.setMessage(announcement.getMessage().getMessage());
                announcementDto.setParty_id(announcement.getMessage().getParty().getId());
                announcementDto.setUsername(announcement.getMessage().getParty().getUser().getUsername());
                announcementDto.setCourse_id(announcement.getCourse().getId());
                announcementDto.setCreatedOn(announcement.getMessage().getCreatedOn());
                announcementDto.setUpdatedOn(announcement.getMessage().getUpdatedOn());
                announcementDto.setComments(null);
                announcementsDto.add(announcementDto);
            }
        }
        return announcementsDto;
    }

    @Transactional
    public long createComment(long discussion_id, DiscussionDto comment) {
        Party party = null;
        try {
            party = partyRepository.findById(comment.getParty_id()).orElseThrow(() -> new PartyNotFoundException("User Not Found"));
        } catch (Exception e) {
            throw e;
        }
        Discussion discussion = discussionRepository.findById(discussion_id).orElseThrow(() -> new DataNotFoundException("Discussion Not Found"));

        IMessage message = new IMessage();
        message.setMessage(comment.getMessage());
        message.setTitle(comment.getTitle());
        message.setCreatedOn(Instant.now());
        message.setUpdatedOn(Instant.now());
        message.setParty(party);

        Comment c = new Comment();
        c.setMessage(message);
        c.setDiscussion(discussion);
        commentRepository.save(c);

        return c.getComment_id();
    }
}
