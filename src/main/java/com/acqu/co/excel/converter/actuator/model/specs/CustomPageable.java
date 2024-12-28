package com.acqu.co.excel.converter.actuator.model.specs;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class CustomPageable {
    private int page=0;
    private int size=20;
    private List<CustomSort> customSort;

    public CustomPageable() {
    }

    // Constructor
    public CustomPageable(int page, int size, List<CustomSort> customSort) {
        this.page = page;
        this.size = size;
        this.customSort = customSort;
    }

    // Getters and setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<CustomSort> getCustomSort() {
        return customSort;
    }

    public void setCustomSort(List<CustomSort> customSort) {
        this.customSort = customSort;
    }

    // Convert CustomPageable to PageRequest
    public Pageable toPageRequest() {
        if(CollectionUtils.isEmpty(customSort)){
            // Return PageRequest with the default sorting criteria
            return PageRequest.of(this.page, this.size);
        }else{
            // Create a Sort.Order array based on the customSort list
            Sort.Order[] orders = new Sort.Order[customSort.size()];

            for (int i = 0; i < customSort.size(); i++) {
                CustomSort sort = customSort.get(i);
                Sort.Order order = sort.getDirection().equalsIgnoreCase("ASC") ?
                        Sort.Order.asc(sort.getField()) :
                        Sort.Order.desc(sort.getField());
                orders[i] = order;
            }

            // Return PageRequest with the multiple sorting criteria
            return PageRequest.of(this.page, this.size, Sort.by(orders));
        }

    }
}

