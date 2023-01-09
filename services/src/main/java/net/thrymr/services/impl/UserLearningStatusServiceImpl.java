package net.thrymr.services.impl;

import net.thrymr.dto.UserLearningStatusDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.UserLearningStatus;
import net.thrymr.repository.UserLearningStatusRepo;
import net.thrymr.services.UserLearningStatusService;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserLearningStatusServiceImpl implements UserLearningStatusService {

    @Autowired
    private UserLearningStatusRepo userLearningStatusRepo;

    @Override
    public String createUserLearningStatus(UserLearningStatusDto request) {

        if (request != null) {
            UserLearningStatus userLearningStatus = new UserLearningStatus();

            if (Validator.isValid(request.getUserId())) {
                userLearningStatus.setUserId(request.getUserId());
            }
            if (Validator.isValid(request.getUserName())) {
                userLearningStatus.setUserName(request.getUserName());
            }
            if (Validator.isValid(request.getUnitSequence())) {
                userLearningStatus.setUnitSequence(request.getUnitSequence());
            }
            if (Validator.isValid(request.getChapterSequence())) {
                userLearningStatus.setChapterSequence(request.getChapterSequence());
            }
            if (Validator.isValid(request.getVideoSequence())) {
                userLearningStatus.setVideoSequence(request.getVideoSequence());
            }
            if (Validator.isValid(request.getTotalUnits())) {
                userLearningStatus.setTotalUnits(request.getTotalUnits());
            }
            if (Validator.isValid(request.getTotalChaptersInUnit())) {
                userLearningStatus.setTotalChaptersInUnit(request.getTotalChaptersInUnit());
            }
            if (Validator.isValid(request.getTotalVideosInChapter())) {
                userLearningStatus.setTotalVideosInChapter(request.getTotalVideosInChapter());
            }
            userLearningStatusRepo.save(userLearningStatus);
            return "User status saved successfully.";
        }
        return "No data found";
    }

    @Override
    public String updateUserLearningStatus(UserLearningStatusDto request) {
        if (request != null) {
            UserLearningStatus userLearningStatus = null;
            if (Validator.isValid(request.getUserId())) {
                Optional<UserLearningStatus> optionalUserLearningStatus = userLearningStatusRepo.findByUserId(request.getUserId());
                if (optionalUserLearningStatus.isPresent()) {
                    userLearningStatus = optionalUserLearningStatus.get();

                    if (Validator.isValid(request.getUserId())) {
                        userLearningStatus.setUserId(request.getUserId());
                    }
                    if (Validator.isValid(request.getUserName())) {
                        userLearningStatus.setUserName(request.getUserName());
                    }
                    if (Validator.isValid(request.getUnitSequence())) {
                        userLearningStatus.setUnitSequence(request.getUnitSequence());
                    }
                    if (Validator.isValid(request.getChapterSequence())) {
                        userLearningStatus.setChapterSequence(request.getChapterSequence());
                    }
                    if (Validator.isValid(request.getVideoSequence())) {
                        userLearningStatus.setVideoSequence(request.getVideoSequence());
                    }
                    if (Validator.isValid(request.getTotalUnits())) {
                        userLearningStatus.setTotalUnits(request.getTotalUnits());
                    }
                    if (Validator.isValid(request.getTotalChaptersInUnit())) {
                        userLearningStatus.setTotalChaptersInUnit(request.getTotalChaptersInUnit());
                    }
                    if (Validator.isValid(request.getTotalVideosInChapter())) {
                        userLearningStatus.setTotalVideosInChapter(request.getTotalVideosInChapter());
                    }
                    userLearningStatusRepo.save(userLearningStatus);
                    return "User status updated successfully";
                } else {
                    userLearningStatus = new UserLearningStatus();

                    if (Validator.isValid(request.getUserId())) {
                        userLearningStatus.setUserId(request.getUserId());
                    }
                    if (Validator.isValid(request.getUserName())) {
                        userLearningStatus.setUserName(request.getUserName());
                    }
                    if (Validator.isValid(request.getUnitSequence())) {
                        userLearningStatus.setUnitSequence(request.getUnitSequence());
                    }
                    if (Validator.isValid(request.getChapterSequence())) {
                        userLearningStatus.setChapterSequence(request.getChapterSequence());
                    }
                    if (Validator.isValid(request.getVideoSequence())) {
                        userLearningStatus.setVideoSequence(request.getVideoSequence());
                    }
                    if (Validator.isValid(request.getTotalUnits())) {
                        userLearningStatus.setTotalUnits(request.getTotalUnits());
                    }
                    if (Validator.isValid(request.getTotalChaptersInUnit())) {
                        userLearningStatus.setTotalChaptersInUnit(request.getTotalChaptersInUnit());
                    }
                    if (Validator.isValid(request.getTotalVideosInChapter())) {
                        userLearningStatus.setTotalVideosInChapter(request.getTotalVideosInChapter());
                    }
                    userLearningStatusRepo.save(userLearningStatus);
                    return "User status saved successfully";
                }
            }
            return "User id not found";
        }
        return "No data found";
    }

    @Override
    public PaginationResponse getAllPaginationUserLearningStatus(UserLearningStatusDto request) {

        Pageable pageable = null;
        if (request.getPageSize() != null && request.getPageNumber() != null) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
        }
        if (request.getPageSize() != null && request.getPageNumber() != null) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "createdOn");
        }
        if (request.getSortUserName() != null && request.getSortUserName().equals(Boolean.TRUE)) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.ASC, "userName");
        } else if (request.getSortUserName() != null && request.getSortUserName().equals(Boolean.FALSE)) {
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "userName");
        }

        Specification<UserLearningStatus> userLearningStatusSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> addUserPredicate = new ArrayList<>();

            if (request.getUserId() != null && !request.getUserId().isEmpty()) {
                Predicate userId = criteriaBuilder.and(root.get("userId").in(request.getUserId()));
                addUserPredicate.add(userId);
            }

            if (request.getUserName() != null) {
                Predicate userName = criteriaBuilder.and(root.get("userName").in(request.getUserName()));
                addUserPredicate.add(userName);
            }

            if (Validator.isValid(request.getUnitSequence())) {
                Predicate unitNumber = criteriaBuilder.and(root.get("unitNumber").in(request.getUnitSequence()));
                addUserPredicate.add(unitNumber);
            }

            if (Validator.isValid(request.getChapterSequence())) {
                Predicate chapterNumber = criteriaBuilder.and(root.get("chapterNumber").in(request.getChapterSequence()));
                addUserPredicate.add(chapterNumber);
            }

            if (request.getVideoSequence() != null) {
                Predicate sequence = criteriaBuilder.and(root.get("sequence").in(request.getVideoSequence()));
                addUserPredicate.add(sequence);
            }

            return criteriaBuilder.and(addUserPredicate.toArray(new Predicate[0]));

        });

        PaginationResponse paginationResponse = new PaginationResponse();

        if (request.getPageSize() != null && request.getPageNumber() != null) {
            Page<UserLearningStatus> userLearningStatuses = userLearningStatusRepo.findAll(userLearningStatusSpecification, pageable);
            if (userLearningStatuses.getContent() != null) {
                paginationResponse.setUserLearningStatusList(userLearningStatuses.getContent());
                paginationResponse.setTotalPages(userLearningStatuses.getTotalPages());
                paginationResponse.setTotalElements(userLearningStatuses.getTotalElements());
                return paginationResponse;
            }
        } else {
            List<UserLearningStatus> userLearningStatusList = userLearningStatusRepo.findAll(userLearningStatusSpecification);
            paginationResponse.setUserLearningStatusList(userLearningStatusList.stream().filter(team -> team.getIsDeleted().equals(Boolean.FALSE)).collect(Collectors.toList()));
            return paginationResponse;
        }

        return new PaginationResponse();
    }


    @Override
    public String deleteUserLearningStatusById(String userId) {

        if(Validator.isValid(userId)) {

            Optional<UserLearningStatus> optionalUserLearningStatus = userLearningStatusRepo.findByUserId(userId);

            if(optionalUserLearningStatus.isPresent()) {
                UserLearningStatus userLearningStatus = optionalUserLearningStatus.get();
                userLearningStatus.setIsActive(Boolean.FALSE);
                userLearningStatus.setIsDeleted(Boolean.TRUE);
                userLearningStatusRepo.save(userLearningStatus);
                return "User status deleted successfully";
            } else {
                return "No data found";
            }
        }

        return "Id not found";
    }
}
