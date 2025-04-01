ALTER TABLE public.boards_columns DROP COLUMN kind;
ALTER TABLE public.boards_columns ADD type varchar(7) NOT NULL;
