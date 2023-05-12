package com.group_1.usege.library.viewModel;

import com.group_1.usege.library.paging.ImagePagingSource;
import com.group_1.usege.library.paging.PagingProvider;
import com.group_1.usege.library.service.MasterFileService;
import com.group_1.usege.library.utilities.mappers.UsegeFilePageResponseMapper;
import com.group_1.usege.model.UserFile;

import java.util.Map;

public class UsegeImageViewModel extends ImageViewModel<Map<String,String>, MasterFileService.QueryResponse<UserFile>> {
    public static final int PAGE_SIZE = 20;
    public static final int MAX_CACHE_PAGE = 5;
    private PagingProvider<Map<String,String>, MasterFileService.QueryResponse<UserFile>> currentProvider;
    public void init(PagingProvider<Map<String,String>, MasterFileService.QueryResponse<UserFile>> provider)
    {
        this.currentProvider = provider;
        super.init(
                PAGE_SIZE,
                MAX_CACHE_PAGE * PAGE_SIZE,
                () -> new ImagePagingSource<Map<String,String>, MasterFileService.QueryResponse<UserFile>>(
                        p -> currentProvider.paging(p),
                        new UsegeFilePageResponseMapper()) {
                    @Override
                    public Map<String,String> getFirstKey() {
                        return null;
                    }
                });
    }
}
