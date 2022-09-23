package net.thrymr.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLQueryResolver;
import net.thrymr.model.master.MtMoodInfo;

@Component
public class Query implements GraphQLQueryResolver {

	
	@Autowired
	private MoodInfoRepository repository;
	
    public MtMoodInfo getMoodInfoById(Long id) {
        return repository.findById(id).orElse(null);
    }
}