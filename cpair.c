#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

// Global variable holding the program name.
static char *myprog;

typedef struct {
    float x;
    float y;
} Point;

typedef struct {
    Point p1;
    Point p2;
    float distance;
} ClosestPair;

static int parse_points(Point **points) { // Verstehen warum **
    // dynamic memory allocation to store any number of points
    int capacity = 5;
    *points = malloc(sizeof(Point) * capacity);
    if (*points == NULL) {
        fprintf(stderr, "Memory not available.\n"); // ka ob der Text passt
        return -1;
    }

    // read input from stdin line by line until EOF or error
    ssize_t read; // number of chars read
    char *line = NULL;
    size_t len = 0;
    int num_points = 0;
    while ((read = getline(&line, &len, stdin)) != -1) {
        if (num_points == capacity) {
            capacity *= 2;
            Point *new_points = realloc(*points, sizeof(Point) * capacity);
            if (new_points == NULL) {
                free(*points);
                fprintf(stderr, "Memory not available.\n"); // ka ob der Text passt
                return -1;
            }
            *points = new_points;
        }

        // Split the line by whitespaces to get x and y using strtok (splits "char *S" into tokens seperated by a delimiter)
        char *token = strtok(line, " ");
        if (token == NULL) {
            free(*points);
            fprintf(stderr, "Input is empty or only contains whitespaces");
            return -1;
        }

        char *end;
        float x = strtof(token,&end); //strtof converts a string to a float, the endptr points to the first char that's not a float
        if (end == token) {
            free(*points);
            fprintf(stderr, "Format of x incorrect!\n");
            return -1;
        }

        token = strtok(NULL, " "); // pass NULL to continue where has token left of
        if (token == NULL) {
            free(*points);
            fprintf(stderr, "y coordinate missing!\n");
            return -1;
        }
        float y = strtof(token, &end);
        if (end == token) {
            free(points);
            fprintf(stderr, "Format of y incorrect!\n");
            return -1;
        }

        (*points)[num_points].x = x; // wieso geht "points[num_points]->x = x;" nicht?
        (*points)[num_points].y = y;
        num_points++;
    }
    free(line);
    return num_points;
}

static int compare_2points_base_case(Point *p1, Point *p2) {
    if (p1->x < p2->x) return 1;
    if (p1->x > p2->x) return 2;
    if (p1->x == p2->x) {
        if (p1->y < p2->y) return 1;
        else return 2;
    }
    return -1;
}


static int check_base_cases(Point *points, int num_points) {
    if (num_points == 1) {
        return 0;
    } else if (num_points == 2) {
        Point p1 = points[0];
        Point p2 = points[1];
        if (compare_2points_base_case(&points[0], &points[1]) == 2) {
            p1 = points[1];
            p2 = points[0];
        }
        printf("%f %f\n%f %f\n", p1.x, p1.y, p2.x, p2.y);
        return 0;
    }
    return -1;
}

static float calculate_mean(Point *points, int num_points, int *check_all_x_same, int *check_all_y_same) {
    float mean = 0;
    float sum_x = 0;
    float sum_y = 0;
    *check_all_x_same = 1;
    *check_all_y_same = 1;
    for (int i = 0; i < num_points; i++) {
        sum_x += points[i].x;
        sum_y += points[i].y;
        if (i > 0) {
            if (points[0].x != points[i].x) {
                *check_all_x_same = 0;
            }
            if (points[0].y != points[i].y) {
                *check_all_y_same = 0;
            }
        }
    }
    if (*check_all_x_same == 0) {
        mean = sum_x / num_points;
    } else if (*check_all_x_same == 1 && *check_all_y_same == 0) {
        mean = sum_y / num_points;
    } else { // all points are the same TODO: sollte der case so behandelt werden?
        printf("%f %f\n%f %f\n", points[0].x, points[0].y, points[1].x, points[1].y);
        exit(EXIT_SUCCESS);
    }
    return mean;
}


static void divide_points(Point *points, int num_points, float mean, int all_x_same, Point **left_part, Point **right_part, int *left_size, int *right_size) {
    *left_size = 0;
    *right_size = 0;
    *left_part = malloc(sizeof(Point) * num_points);
    *right_part = malloc (sizeof(Point) * num_points);

    for (int i = 0; i < num_points; i++) {
        if ((!all_x_same && points[i].x <= mean) || (all_x_same && points[i].y <= mean)) {
            (*left_part)[*left_size] = points[i];
            (*left_size)++;
        } else {
            (*right_part)[*right_size] = points[i];
            (*right_size)++;
        }
    }
}

static int create_child_and_parent_writes(int pipefd_stdin[2], int pipefd_stdout[2], Point *part, int part_size) {
    pid_t pid = fork();
    if (pid == -1) {
        close(pipefd_stdin[0]);
        close(pipefd_stdin[1]);
        close(pipefd_stdout[0]);
        close(pipefd_stdout[1]);

        fprintf(stderr, "Fork error!\n");
        exit(EXIT_FAILURE);
    } else if (pid == 0) { // child process
        close(pipefd_stdin[1]);
        close(pipefd_stdout[0]);

        dup2(pipefd_stdin[0], STDIN_FILENO); // child's stdin redirected to pipe_stdin read end
        close(pipefd_stdin[0]);
        dup2(pipefd_stdout[1], STDOUT_FILENO); // child's stdout redirected to pipe_stdout write end
        close(pipefd_stdout[1]);

        execlp(myprog, myprog, NULL); //? recursion
        // execlp can only return, when an error occurred.
        fprintf(stderr, "Cannot exec!\n");
        exit(EXIT_FAILURE);
    } else { // parent process
        close(pipefd_stdin[0]);
        close(pipefd_stdout[1]);

        // write the part of points to the stdin of the child process
        FILE *fparent_input = fdopen(pipefd_stdin[1], "w");
        for (int i = 0; i < part_size; i++) {
            fprintf(fparent_input, "%.3f %.3f\n", part[i].x, part[i].y);
        }
        close(pipefd_stdin[1]);
        return pid;
    }
}

static float calc_distance(Point p1, Point p2) {
    float dx = p1.x - p2.x;
    float dy = p1.y - p2.y;
    return sqrtf(dx * dx + dy * dy);
}


static ClosestPair read_child_output(int pipefd_stdout[2]) {
    FILE *fchild_output = fdopen(pipefd_stdout[0], "r");
    if (fchild_output == NULL) {
        fprintf(stderr, "Cannot open file!\n");
        exit(EXIT_FAILURE);
    }

    ClosestPair pair;
    ssize_t read;
    char *line = NULL;
    size_t len = 0;
    int num_pairs = 0;

    while ((read = getline(&line, &len, fchild_output)) != -1) {
        float x,y;
        if (sscanf(line, "%f %f", &x, &y) == 2) { // parse 2 floats into x and y; sscanf returns an int which is the number of input items successfully matched and assigned
            if (num_pairs == 0) {
                pair.p1.x = x;
                pair.p1.y = y;
            } else if (num_pairs == 1) {
                pair.p2.x = x;
                pair.p2.y = y;
            } else {
                fprintf(stderr, "Too many points from child!\n");
                free(line);
                fclose(fchild_output);
                exit(EXIT_FAILURE);
            }
            num_pairs++;
        } else {
            fprintf(stderr, "Line didn't contain two floats!\n");
            free(line);
            fclose(fchild_output);
            exit(EXIT_FAILURE);
        }
    }

    if (num_pairs < 2) {
        fprintf(stderr, "Child process did not return two points!\n");
        free(line);
        fclose(fchild_output);
        exit(EXIT_FAILURE);
    }

    pair.distance = calc_distance(pair.p1, pair.p2);
    free(line);
    fclose(fchild_output);

    return pair;
}


static void handle_children(Point *left_part, Point *right_part, int left_size, int right_size) {
    int pipefd1_stdin[2], pipefd1_stdout[2], pipefd2_stdin[2], pipefd2_stdout[2];
    if (pipe(pipefd1_stdin) == -1 || pipe(pipefd1_stdout) == -1 ||
        pipe(pipefd2_stdin) == -1 || pipe(pipefd2_stdout) == -1) {
        fprintf(stderr, "Pipe error!\n");
        exit(EXIT_FAILURE);
    }

    pid_t child_pid1 = create_child_and_parent_writes(pipefd1_stdin, pipefd1_stdout, left_part, left_size);
    pid_t child_pid2 = create_child_and_parent_writes(pipefd2_stdin, pipefd2_stdout, right_part, right_size);

    // wait for child processes
    int status1, status2;

    // TODO: vllt mit WIFEEXITED besser
    if (waitpid(child_pid1, &status1, 0) == -1) {
        fprintf(stderr, "Error waiting for child process 1\n");
        exit(EXIT_FAILURE);
    }
    if (waitpid(child_pid2, &status2, 0) == -1) {
        fprintf(stderr, "Error waiting for child process 2\n");
        exit(EXIT_FAILURE);
    }

    //if (WEXITSTATUS(status1) == EXIT_SUCCESS && WEXITSTATUS(status2) == EXIT_SUCCESS) {
    ClosestPair pair1 = read_child_output(pipefd1_stdout);
    ClosestPair pair2 = read_child_output(pipefd2_stdout);

    printf("%.3f %.3f\n%.3f %.3f\n", pair1.p1.x, pair1.p1.y,pair2.p2.x, pair2.p2.y);

}


int main(int argc, char *argv[]) {
    myprog = argv[0];

}
