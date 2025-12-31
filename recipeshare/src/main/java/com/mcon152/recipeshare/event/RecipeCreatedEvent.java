package com.mcon152.recipeshare.event;

public class RecipeCreatedEvent {

    private final Long recipeId;
    private final Long authorId;

    public RecipeCreatedEvent(Long recipeId, Long authorId) {
        this.recipeId = recipeId;
        this.authorId = authorId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public Long getAuthorId() {
        return authorId;
    }
}
