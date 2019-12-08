# Preview Generation ICS
## Introduction
This Interoperability Conformance Specificaion (ICS) specifies the communication between a cloud based Preview Generation Service (Worker) the Consumer (Manager) of the service.

This ICS document describes in particular the request to the service (Worker) for a preview generation of a PDF document as well as the response, which also contains the generated preview image.

### Specification of Cardinality
The following table illustrates the notation of Manager and Worker requirements in ICS tables:

| Notation | Name | Description |
| --- | --- | --- |
| w | Write Required | The element or attribute SHALL be written by the Manager or Worker |
| w? | Write Optional | The element or attribute MAY be written by the Manager or Worker |
| w← | Write Conditional | The element or attribute SHALL be written by the Manager or Worker depending on conditions. The details of the conditions will be specified in the description. |
| w! | Write Forbidden | The element or attribute SHALL NOT be written by the Manager or Worker. |
| r | Read Required | The element or attribute SHALL be read by the Manager or Worker. |
| r? | Read Optional | The element or attribute MAY be read by the Manager or Worker. |
| r← | Read Conditional | The element or attribute SHALL be read by the Manager or Worker depending on conditions. The details of the conditions will be specified in the description. |

### General Architecture
All transactions SHALL conform to XKDF. 

#### Transport Protocol
Manager and Worker SHALL implement a synchronous http transport protocol. The Preview Service (Worker) SHALL be accessible by the Manager, but not vice versa.

#### Packaging
Each transaction SHALL be packaged as a ZIP Package containing all dependencies. Dependencies outside the ZIP Packages are not valid.
In general a ZIP package contains a XJMF Message called *root.xjmf', an XJDF Document as well as additional assets referred by the XJDF Document.

## XJMF Message
Specificaion of the XJMF Message.

### Element: XJMF
The XJMF element SHALL be the root element of the XJMF Message.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @xmlns | w <br> r | r <br> w | The value SHALL be 'http://www.CIP4.org/JDFSchema_2_0' |
| Header | w <br> r | r <br> w | Element containing informatoin about the parties involved in the transaction |
| CommandSubmitQueueEntry | w | r | Element has to be written by the Manager during a Request |
| CommandReturnQueueEntry | r | w | Element has to be written by the Worker during a Response |

### Element: Header
The Header element is a container element for information about the parties that are involved in the transaction.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @DeviceID |  w <br> r? | r? <br> w | The devices identifier which generated the parent element. |
| @Time |  w <br> r? | r? <br> w | The timestamp when the parent element has been generated |

### Element: CommandSubmitQueueEntry
The XJMF Command SubmitQueueEntry requests a new preview generation.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| Header | w | r? | Information about the Manager |
| QueueSubmissionParams | w | r | Details about the perview generation request |

#### Element: QueueSubmissionParams
Details about the preview generation request.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @URL | w | r | Reference to the XJDF Document within the ZIP Package. |

### Element: CommandReturnQueueEntry
The XJMF Command ReturnQueueEntry is a synchronuos response of a preview generation.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| Header | r? | w | Information about the Worker |
| ReturnQueueEntryParams | r | w | Details about the preview generation result |

#### Element: ReturnQueueEntryParams
Details about the preview generation response.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @URL | r | w | Reference to the XJDF Document within the ZIP Package. |

## XJDF Document
Specificaion of the XJDF Document containing the preview generation details.

### Element: XJDF
The XJMF element SHALL be the root element of the XJDF Document.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @JobID | w <br> r? | r? <br> w | The jobs identifier. |
| @Types | w <br> r | r <br> w | The value SHALL be "Interpreting Rendering PreviewGeneration" | 
| @ICSVersions | w <br> r | r <br> w | The unique identifier of this ICS Document: "PREVIEW_ICS" |
| @xmlns | w <br> r | r <br> w | The XJDF 2.0 namespace specificaion: http://www.CIP4.org/JDFSchema_2_0 |
| AuditPool | r | w   | Container element for metadata as well as feedback from the Worker to the Manager |
| ResourceSet [@Name="RunList"] | w | r | Reference to the customers artwork PDF |
| ResourceSet [@Name="PreviewGenerationParams"] | w| r | Paramters regarding to the preview generation. |

### Element: AuditPool
Container element for feedback from the Worker to the Manager.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| AuditResource | r | w | Container element for resources generated by the Worker  | 

#### Subelement: AuditResource
Container element for resources generated by the Worker. 

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| Header | r | w | Metadata about the worker. |
| ResourceInfo | r | w | Container element for a single ResourceSet |

#### Subelement: ResourceInfo
Container elemnet for a single ResourceSet.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| ResourceSet | r | w | Container for a Preview Resource |


### Element: ResourceSet
Container element for similar resources.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @Name | w <br> r | r <br> w | The name of the Specific Resource (see XJDF Spec.) |
| @Usage | w <br> r | r <br> w | The usage of the Resource (see XJDF Spec.) <br> Valid values SHALL be "Input" and "Output" |
| Resource | w <br> r | r <br> w | Container for the specific Resource |

### Element: Resource
Container element for a single specific Resource (see XJDF Spec)

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| *Specific Resource* | w <br> r | r <br> w | The specific resource element |

### Specific Resource: RunList
A RunList Resource references binary data.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| FileSpec | w | r | The reference to the file |

#### Subelement: FileSpec
Reference to a single file.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @URL | w | r | The reference to the artwork PDF |

### Specific Resource: PreviewGenerationParams
Details and requirements regarding to the preview generation.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @Resolution | w | r | The required resolution of the preview in x- and y-directions. |


### Specific Resource: Preview
The details and reference to the generated preview image.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @PreviewFileType | r | w | The type of the preview image. Allowed value is "PNG" |
| FileSpec | r | w | Reference to the preview binary data |

#### Subelement: FileSpec
Reference to the preview file.

| Name | Manager | Worker | Description |
| --- | --- | --- | --- |
| @URL | r | w | The reference to the preview file |

## Appendix

### Sample Request
The XJMF Command Message *SubmitQueueEntry* in the request ZIP package in order to submit the referenced XJDF Document.

```xml
<XJMF xmlns="http://www.CIP4.org/JDFSchema_2_0">
    <Header DeviceID="MY_DEV_ID" Time="2019-10-15T21:26:07+02:00" />
    <CommandSubmitQueueEntry>
        <Header DeviceID="MY_DEV_ID" Time="2019-10-15T21:26:07+02:00"/>
        <QueueSubmissionParams URL="preview-request.xjdf"/>
    </CommandSubmitQueueEntry>
</XJMF>
```

The XJDF Document in the request ZIP package referenced by the XJDF Command Message *SubmitQueueEntry*. The document contains all information needed for the preview generation.

```xml
<XJDF JobID="42" Types="Interpreting Rendering PreviewGeneration" ICSVersions="PREVIEW_ICS" xmlns="http://www.CIP4.org/JDFSchema_2_0">
    <AuditPool>
        <AuditCreated>
            <Header DeviceID="DEV_ID" Time="2019-10-15T21:26:07+02:00" />
        </AuditCreated>
    </AuditPool>
    <ResourceSet Name="RunList" Usage="Input">
        <Resource>
            <RunList>
                <FileSpec URL="file-1.pdf" />
            </RunList>
        </Resource>
    </ResourceSet>
    <ResourceSet Name="PreviewGenerationParams" Usage="Input">
        <Resource>
            <PreviewGenerationParams Resolution="72 72" />
        </Resource>
    </ResourceSet>
</XJDF>
```

### Sample Response
The XJMF Command Message ReturnQueueEntry in the response ZIP package in order to return the referenced XJDF Document.
```xml
<xjdf:XJMF xmlns:xjdf="http://www.CIP4.org/JDFSchema_2_0">
    <xjdf:Header DeviceID="PreviewService" Time="2019-11-19T13:32:24Z"/>
    <xjdf:CommandReturnQueueEntry>
        <xjdf:Header DeviceID="PreviewService" Time="2019-11-19T13:32:24Z"/>
        <xjdf:ReturnQueueEntryParams QueueEntryID="QE-42" URL="myUrl"/>
    </xjdf:CommandReturnQueueEntry>
</xjdf:XJMF>
```

The XJDF Document in the response ZIP package referenced by the XJDF Command Message *ReturnQueueEntry*. The document contains all information regarding to the preview generation.

```xml
<xjdf:XJDF ICSVersions="PREVIEW_ICS" JobID="42" Types="Interpreting Rendering PreviewGeneration" xmlns:xjdf="http://www.CIP4.org/JDFSchema_2_0">
    <xjdf:AuditPool>
        <xjdf:AuditCreated>
            <xjdf:Header DeviceID="PreviewService" Time="2019-11-19T13:32:24Z"/>
        </xjdf:AuditCreated>
        <xjdf:AuditResource>
            <xjdf:Header DeviceID="PreviewService" Time="2019-11-19T13:32:24Z"/>
            <xjdf:ResourceInfo>
                <xjdf:ResourceSet Name="Preview" Usage="Output">
                    <xjdf:Resource>
                        <xjdf:Preview PreviewFileType="PNG">
                            <xjdf:FileSpec URL="preview.png"/>
                        </xjdf:Preview>
                    </xjdf:Resource>
                </xjdf:ResourceSet>
            </xjdf:ResourceInfo>
        </xjdf:AuditResource>
    </xjdf:AuditPool>
    <xjdf:ResourceSet Name="RunList" Usage="Input">
        <xjdf:Resource>
            <xjdf:RunList>
                <xjdf:FileSpec URL="tu-dublin.pdf"/>
            </xjdf:RunList>
        </xjdf:Resource>
    </xjdf:ResourceSet>
    <xjdf:ResourceSet Name="PreviewGenerationParams" Usage="Input">
        <xjdf:Resource>
            <xjdf:PreviewGenerationParams Resolution="72.0 72.0"/>
        </xjdf:Resource>
    </xjdf:ResourceSet>
</xjdf:XJDF>
```