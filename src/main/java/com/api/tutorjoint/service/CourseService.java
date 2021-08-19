package com.api.tutorjoint.service;


import com.api.tutorjoint.exception.CourseNotFoundException;
import com.api.tutorjoint.model.*;
import com.api.tutorjoint.model.Module;
import com.api.tutorjoint.repository.*;
import com.api.tutorjoint.exception.PartyNotFoundException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.api.tutorjoint.dto.CourseDto;
import com.api.tutorjoint.dto.ModuleDto;
import com.api.tutorjoint.dto.AvatarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CourseService {


    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private PartyService partyService;

    @Transactional
    public void createCourse(CourseDto courseDto) throws PartyNotFoundException {
        Party party = null;
        try {
            party = partyService.getPartyForCurrentUser();

        } catch (Exception e) {
            throw new PartyNotFoundException("Party record does not exist for current user");
        }

        Tutor tutor = tutorRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Tutor Not Found"));

        Course course = new Course();
        course.setTutor(tutor);
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setCategory(String.join(",", courseDto.getCategory()));
        course.setCreatedOn(Instant.now());
        course.setUpdatedOn(Instant.now());

        AvatarDto avatarDto = courseDto.getAvatar();
        Image img = saveAvatar(avatarDto);
        course.setImageId(img.getId());

        courseRepository.save(course);

        String[] arrGoal = courseDto.getGoals();
        //For each Goal
        for (String elem : arrGoal) {
            Goal goal = new Goal();
            goal.setDescription(elem);
            goal.setCreatedOn(Instant.now());
            goal.setUpdatedOn(Instant.now());
            goal.setCourse(course);
            goalRepository.save(goal);
        }

        ModuleDto[] arrModules = courseDto.getModules();
        //For each Module
        for (ModuleDto elem : arrModules) {
            Module module = new Module();
            module.setDescription(elem.getDescription());
            module.setCreatedOn(Instant.now());
            module.setUpdatedOn(Instant.now());
            module.setCourse(course);
            moduleRepository.save(module);

            String[] arrLessons = elem.getLessons();
            //For each Lesson
            for (String elem1 : arrLessons) {
                Lesson lesson = new Lesson();
                lesson.setDescription(elem1);
                lesson.setCreatedOn(Instant.now());
                lesson.setUpdatedOn(Instant.now());
                lesson.setModule(module);
                lessonRepository.save(lesson);
            }
        }
    }

    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::getCourseDto).collect(toList());
    }

    public CourseDto getCourse(Long id) throws CourseNotFoundException {
        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("For id " + id));
        return getCourseDto(course);
    }

    public List<CourseDto> getCoursesByTutor(Long Id) {
        Party party = null;
        try {
            party = partyService.getPartyById(Id);
        } catch (Exception e) {
            throw new PartyNotFoundException("Party record does not exist for current user");
        }
        Tutor tutor = tutorRepository.findByParty(party).orElseThrow(() -> new PartyNotFoundException("Tutor record does not exist for current user"));

        //Get Courses
        List<Course> courses = courseRepository.findByTutor(tutor, Sort.by(Sort.Direction.ASC, "id"));
        return courses.stream().map(this::getCourseDto).collect(toList());
    }

    public CourseDto getCourseDto(Course course) {

        //Instantiate CourseDto
        CourseDto courseDto = new CourseDto();
        courseDto.setDescription(course.getDescription());
        courseDto.setCategory(null == course.getCategory() ? null : course.getCategory().split(","));
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());

        Tutor tutor = course.getTutor();
        Party party = tutor.getParty();
        courseDto.setTutor_id(Long.toString(party.getId()));

        courseDto.setId(course.getId());

        //Set Course Avatar
        Long imageId = course.getImageId();
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new CourseNotFoundException("Course Avatar for id " + course.getId()));
        AvatarDto avatarDto = new AvatarDto();
        avatarDto.setName(image.getName());
        avatarDto.setType(image.getType());
        avatarDto.setBase64String(image.getBase64String());
        courseDto.setAvatar(avatarDto);

        //Set Goals
        List<Goal> lstGoals = goalRepository.findByCourse(course, Sort.by(Sort.Direction.ASC, "id"));
        List<String> lstGoalsDto = new ArrayList<String>();
        for (Goal goal : lstGoals) {
            lstGoalsDto.add(goal.getDescription());
        }
        String arrGoals[] = lstGoalsDto.toArray(new String[lstGoalsDto.size()]);
        courseDto.setGoals(arrGoals);

        //Set Modules
        List<Module> lstModules = moduleRepository.findByCourse(course, Sort.by(Sort.Direction.ASC, "id"));
        List<ModuleDto> lstModuleDto = new ArrayList<ModuleDto>();

        for (Module module : lstModules) {
            List<Lesson> lstLessons = lessonRepository.findByModule(module, Sort.by(Sort.Direction.ASC, "id"));
            List<String> lstLessonDto = new ArrayList<String>();
            for (Lesson lesson : lstLessons) {
                lstLessonDto.add(lesson.getDescription());
            }
            String arrLessons[] = lstLessonDto.toArray(new String[lstLessonDto.size()]);

            ModuleDto moduleDto = new ModuleDto();
            moduleDto.setDescription(module.getDescription());
            moduleDto.setLessons(arrLessons);
            lstModuleDto.add(moduleDto);
        }
        ModuleDto arrModules[] = lstModuleDto.toArray(new ModuleDto[lstModuleDto.size()]);
        courseDto.setModules(arrModules);
        return courseDto;
    }

    @Transactional
    public Image saveAvatar(AvatarDto avatarDto) {
        Image image = new Image();

        image.setBase64String(avatarDto.getBase64String());
        image.setName(avatarDto.getName());
        image.setCreatedOn(Instant.now());
        image.setUpdatedOn(Instant.now());

        return imageRepository.save(image);
    }

    public List<Goal> getGoals(CourseDto courseDto) {
        String[] arrGoal = courseDto.getGoals();
        List<Goal> goals = new ArrayList<Goal>(arrGoal.length);

        //For each Goal
        for (String elem : arrGoal) {
            Goal goal = new Goal();
            goal.setDescription(elem);
            goal.setCreatedOn(Instant.now());
            goal.setUpdatedOn(Instant.now());
            goals.add(goal);
        }
        return goals;
    }


}
