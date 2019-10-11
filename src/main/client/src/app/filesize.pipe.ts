import { Pipe, PipeTransform } from '@angular/core';
/*
 * File size formatting pipe.
*/
@Pipe({ name: 'fileSize' })
export class FileSizePipe implements PipeTransform {

    /**
     * Transform a number to a file size string. Source: https://stackoverflow.com/a/18650828/5678557
     * @param bytes the bytes as long
     * @param decimals the number of decimals of the result.
     */
    transform(bytes: number, decimals?: number): String {
        if (bytes === 0) return '0 Bytes';

        const k = 1024;
        const dm = decimals < 0 ? 0 : decimals;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

        const i = Math.floor(Math.log(bytes) / Math.log(k));

        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    }

}