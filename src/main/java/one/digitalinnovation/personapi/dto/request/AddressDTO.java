package one.digitalinnovation.personapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long id;

    @NotEmpty
    @Size(min = 9, max = 9)
    private String cep;

    @NotEmpty
    @Size(min = 4, max = 100)
    private String address;

    @NotEmpty
    @Size(min = 4, max = 100)
    private String neighborhood;

    @NotEmpty
    @Size(min = 4, max = 100)
    private String city;

    @NotEmpty
    @Size(min = 2, max = 2)
    private String state;
}
