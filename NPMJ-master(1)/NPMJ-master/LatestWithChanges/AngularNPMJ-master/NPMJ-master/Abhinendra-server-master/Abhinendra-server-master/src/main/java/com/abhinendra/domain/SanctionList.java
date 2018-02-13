package com.abhinendra.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
//@Table(name="SanctionList")
@Table(name = "SanctionList")

public class SanctionList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The database generated person ID")
    private int id;

    @Column(name="Name")
    @ApiModelProperty(notes = "The name of  person", required = true)
    private String name;

    public  SanctionList(){
    }
    public SanctionList(String name){
        this.name = name;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}



