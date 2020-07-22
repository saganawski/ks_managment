package com.ks.management.recruitment.interview.controller;

import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.service.InterviewService;
import com.ks.management.recruitment.interview.ui.InterviewApplicationDto;
import com.ks.management.recruitment.interview.ui.InterviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviews")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;

    @GetMapping()
    public List<Interview> getInterviews(){
        return interviewService.getAllInterviews();
    }

    @PostMapping()
    public Interview createInterview(@RequestBody Interview interview){
        System.out.println("interview = " + interview);
        return  interviewService.createInterview(interview);
    }

    @GetMapping("/{interviewId}/dto")
    public InterviewApplicationDto getInterviewApplicationDto(@PathVariable("interviewId")int interviewId){
        return interviewService.findInterviewApplicationDto(interviewId);
    }

    @PutMapping("/{interviewId}")
    public Interview updateInterview(@PathVariable("interviewId") Integer interviewId, @RequestBody InterviewDto interviewDto){
        if(!interviewId.equals(interviewDto.getId())){
            throw new RuntimeException("Id in path does not match request body");
        }

        return interviewService.updateInterview(interviewDto);
    }

    @DeleteMapping("/{interviewId}/notes/{noteId}")
    public void deleteNoteForInterview(@PathVariable("interviewId") int interviewId, @PathVariable("noteId") int noteId){
        interviewService.deleteNote(noteId);
    }

    @DeleteMapping("/{interviewId}")
    public void deleteInterview(@PathVariable("interviewId")int interviewId){
        interviewService.deleteInterview(interviewId);
    }
}
