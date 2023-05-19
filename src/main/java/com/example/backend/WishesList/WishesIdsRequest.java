package com.example.backend.WishesList;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishesIdsRequest {
    private List<UUID> ids;
}
