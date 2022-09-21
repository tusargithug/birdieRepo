package net.thrymr.services.impl;

import net.thrymr.dto.AssignmentDto;
import net.thrymr.dto.QuestionDto;
import net.thrymr.model.master.MtAssignment;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.repository.AssignmentRepo;
import net.thrymr.services.AssignmentService;
import net.thrymr.utils.ApiResponse;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {


    private final AssignmentRepo assignmentRepo;

    private final Environment environment;

    public AssignmentServiceImpl(AssignmentRepo assignmentRepo, Environment environment) {
        this.assignmentRepo = assignmentRepo;
        this.environment = environment;
    }
    // get all assignment
    @Override
    public ApiResponse getAllAssignment() {
        List<MtAssignment> mtAssignmentList =assignmentRepo.findAll();
        if(!mtAssignmentList.isEmpty()){
            //TODO based on reference video
            List<AssignmentDto>assignmentDtoList= mtAssignmentList.stream().map(this::entityToDto).toList();
            return new ApiResponse(HttpStatus.OK,environment.getProperty("SUCCESS"),assignmentDtoList);

        }

        return new ApiResponse(HttpStatus.OK,environment.getProperty("ASSIGNMENT_NOT_FOUND"));
    }

    private AssignmentDto entityToDto(MtAssignment request){
        AssignmentDto dto=new AssignmentDto();
        dto.setId(request.getId());
        dto.setQuestionDtoList(request.getMtQuestions().stream().map(this::entityToDto).toList());
       return dto;
    }
    private QuestionDto entityToDto(MtQuestion request){
        QuestionDto dto=new QuestionDto();
        dto.setId(request.getId());
        dto.setQuestion(request.getQuestion());

        return dto;
    }
}
