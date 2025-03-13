package in.usha.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.usha.dto.QuoteApiResponseDTO;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	
	private String quoteApiUrl = "https://dummyjson.com/quotes/random";

	@Override
	public QuoteApiResponseDTO getQuote() {
		
		RestTemplate rt = new RestTemplate();
		ResponseEntity<QuoteApiResponseDTO> forEntity = rt.getForEntity(quoteApiUrl, QuoteApiResponseDTO.class);
		
		QuoteApiResponseDTO body = forEntity.getBody();
		
		return body;
	}

}
