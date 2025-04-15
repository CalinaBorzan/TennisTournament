package org.example.tennisapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.tennisapp.entity.Match;

import java.util.List;

public class JsonMatchExporter implements MatchExportStrategy{

    @Override
    public String export(List<Match> matches)
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(matches);

        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Failed to export matches to JSON",e);

        }
    }
}
