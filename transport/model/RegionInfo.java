package transport.model;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class RegionInfo {
    private Integer id;
    private Integer type;
    private Integer capitalId;
    private Integer[] hierarchy;
    private String seoname;
    private Double[][] bounds;
    private Double longitude;
    private Double latitude;
    private Integer zoom;
    private Names names;
    private RegionInfo parentRegion;
    private String title;
    private String subtitle;
}