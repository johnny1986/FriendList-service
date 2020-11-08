package com.afilias.friendlist.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull()).setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);
        return modelMapper;
    }


}
